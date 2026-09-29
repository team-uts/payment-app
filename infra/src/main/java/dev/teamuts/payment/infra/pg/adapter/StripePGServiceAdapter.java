package dev.teamuts.payment.infra.pg.adapter;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPGServicePort;
import dev.teamuts.payment.infra.common.annotation.PGAdapter;
import dev.teamuts.payment.infra.pg.constant.ExtPGOperationType;
import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import dev.teamuts.payment.infra.pg.dto.StripeWebhookPayload;
import dev.teamuts.payment.infra.pg.utils.StripeWebhookSecretManager;
import dev.teamuts.payment.infra.pg.webhook.stripe.StripeWebhookEventMapperProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PGAdapter
@RequiredArgsConstructor
public class StripePGServiceAdapter implements ExternalPGServicePort {
  private static final PGProviderType PG_PROVIDER = PGProviderType.STRIPE;
  private static final String WEBHOOK_HEADER_NAME = "Stripe-Signature";

  private final StripeClient stripeClient;
  private final StripeWebhookSecretManager webhookSecretManager;
  private final StripeWebhookEventMapperProvider webhookEventMapperProvider;

  @Override
  public boolean supports(PGProviderType key) {
    return key == PG_PROVIDER;
  }

  /**
   * Creates a new Customer object in Stripe. (AccountV2 is not supported by Stripe for sandbox.)
   *
   * @param memberId Member ID in the database
   * @param email Email address of the member
   * @return Account DTO from PG containing the created Stripe "Customer ID"
   */
  @Override
  public ExtPGAccountDto createNewAccount(Long memberId, String email) {
    ExtPGOperationType operationType = ExtPGOperationType.CREATE_CUSTOMER;

    CustomerCreateParams params =
        CustomerCreateParams.builder()
            .setEmail(email)
            .setName(memberId.toString())
            .putMetadata(operationType.getMetadataKey(), memberId.toString())
            .build();

    try {
      Customer customer = stripeClient.v1().customers().create(params);
      String userId = customer.getName();

      return ExtPGAccountDto.builder()
          .memberId(Long.parseLong(userId))
          .pgAccountId(customer.getId())
          .pgProvider(PG_PROVIDER)
          .build();
    } catch (Exception e) {
      log.error(e.getMessage());

      throw new RuntimeException("[%s] %s - failed".formatted(PG_PROVIDER, operationType));
    }
  }

  /**
   * Sets up a payment method request for the given PG account.
   *
   * @param pgAccount The PG account for which to set up the payment method. It is managed in the
   *     database and contains the "Stripe Customer ID" (not using AccountV2).
   * @return The result of the payment method setup.
   */
  @Override
  public ExtPGPaymentMethodOperationDto setupPaymentMethodRequest(PGAccount pgAccount) {
    ExtPGOperationType operationType = ExtPGOperationType.CREATE_SETUP_INTENT;

    // Parms: **Stripe Customer ID** (not AccountV2 ID)
    // Parms: Member ID (for metadata)
    SetupIntentCreateParams params =
        SetupIntentCreateParams.builder()
            .setCustomer(pgAccount.getPgAccountId())
            .setAutomaticPaymentMethods(
                SetupIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
            .putMetadata(operationType.getMetadataKey(), pgAccount.getMemberId().toString())
            .build();

    try {
      SetupIntent setupIntent = stripeClient.v1().setupIntents().create(params);
      String userId = setupIntent.getMetadata().get(operationType.getMetadataKey());

      return ExtPGPaymentMethodOperationDto.builder()
          .memberId(Long.parseLong(userId))
          .pgOperationId(setupIntent.getId())
          .pgOperationName(operationType.name())
          .pgAccountId(setupIntent.getCustomer())
          .pgProvider(PG_PROVIDER)
          .pgProviderSecret(setupIntent.getClientSecret())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage());

      throw new RuntimeException("[%s] %s - failed".formatted(PG_PROVIDER, operationType));
    }
  }

  @Override
  public void setupPaymentRequest() {}

  @Override
  public void confirmPaymentRequest() {}

  @Override
  public String getWebhookHeaderName() {
    return WEBHOOK_HEADER_NAME;
  }

  /**
   * Parses a webhook event from the Stripe API based on the {@link StripeWebhookEventType}.
   *
   * @param requestType The type of request that the webhook event corresponds to.
   * @param payload The raw payload from the webhook.
   * @param secret The signature from the webhook header.
   */
  @Override
  public ExtPGWebhookEventDto parseWebhookEvent(
      PGRequestType requestType, String payload, String secret) {
    String endpointSecret = webhookSecretManager.getEndpointSecret(requestType);

    try {
      Event event = stripeClient.constructEvent(payload, secret, endpointSecret);
      StripeWebhookEventType eventType = StripeWebhookEventType.fromEventTypeName(event.getType());

      // convert to StripeWebhookPayload based on the StripeWebhookEventType
      StripeWebhookPayload stripePayload =
          webhookEventMapperProvider.getInstance(eventType).convert(event);

      return ExtPGWebhookEventDto.of(PG_PROVIDER, requestType, stripePayload);
    } catch (SignatureVerificationException e) {
      log.error(e.getMessage());

      // TODO: need to handle this exception properly, maybe return a 400 response to Stripe
      throw new RuntimeException("[%s] Webhook verification failed".formatted(PG_PROVIDER));
    } catch (Exception e) {
      log.error(e.getMessage());

      throw new RuntimeException("[%s] Webhook payload parsing failed".formatted(PG_PROVIDER));
    }
  }

  @Override
  public PGExternalRequest findBaseExternalRequestForWebhook(
      List<PGExternalRequest> pgExternalRequests, PGRequestType requestType) {
    // Find the base operation type for the given request type
    // e.g., for PAYMENT_METHOD_SETUP, the base operation type is CREATE_SETUP_INTENT
    ExtPGOperationType baseOperationType =
        ExtPGOperationType.getBaseOperationTypeFromRequestType(requestType);

    // After filtering the list, it should have exactly a single element.
    return pgExternalRequests.stream()
        .filter(req -> baseOperationType.name().equals(req.getExtOperation()))
        .findFirst() // It's going to only use the element.
        .orElseThrow(
            () ->
                new RuntimeException(
                    "[%s] No base external request found for request type: %s"
                        .formatted(PG_PROVIDER, requestType)));
  }

  @Override
  public ExtPGPaymentMethodDto retrievePGPaymentMethod(
      PGAccount pgAccount, String pgPaymentMethodId) {
    try {
      PaymentMethod pgPaymentMethod =
          stripeClient
              .v1()
              .customers()
              .paymentMethods()
              .retrieve(pgAccount.getPgAccountId(), pgPaymentMethodId);

    } catch (StripeException e) {
      log.error(e.getMessage());

      throw new RuntimeException(
          "[%s] %s - failed".formatted(PG_PROVIDER, "Retrieve Payment Method"));
    }

    return null;
  }
}
