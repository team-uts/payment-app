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
import dev.teamuts.payment.domain.pg.port.infra.ExternalPGServicePort;
import dev.teamuts.payment.infra.common.annotation.PGAdapter;
import dev.teamuts.payment.infra.pg.constant.StripeOperationType;
import dev.teamuts.payment.infra.pg.constant.StripePaymentMethodType;
import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import dev.teamuts.payment.infra.pg.dto.StripeWebhookPayload;
import dev.teamuts.payment.infra.pg.utils.StripeWebhookSecretManager;
import dev.teamuts.payment.infra.pg.webhook.stripe.StripeWebhookEventMapperProvider;
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
    StripeOperationType operationType = StripeOperationType.CREATE_CUSTOMER;

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
    StripeOperationType operationType = StripeOperationType.CREATE_SETUP_INTENT;

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

  /**
   * Retrieves a payment method from Stripe using the given PG account and payment method ID.
   *
   * <p>Here, we can check the payment method type (e.g., card, bank account).
   *
   * @param pgAccount The PG account containing the Stripe Customer ID.
   * @param pgPaymentMethodId The ID of the payment method to retrieve.
   * @return The retrieved payment method DTO.
   */
  @Override
  public ExtPGPaymentMethodDto retrievePGPaymentMethod(
      PGAccount pgAccount, String pgPaymentMethodId) {
    try {
      PaymentMethod stripePaymentMethod =
          stripeClient
              .v1()
              .customers()
              .paymentMethods()
              .retrieve(pgAccount.getPgAccountId(), pgPaymentMethodId);

      StripePaymentMethodType stripePaymentMethodType =
          StripePaymentMethodType.fromTypeName(stripePaymentMethod.getType());

      return ExtPGPaymentMethodDto.builder()
          .memberId(pgAccount.getMemberId())
          .pgPaymentMethodId(stripePaymentMethod.getId())
          .pgProvider(PG_PROVIDER)
          .detail(stripePaymentMethodType.getConverter().apply(stripePaymentMethod))
          .build();
    } catch (StripeException e) {
      log.error(e.getMessage());

      throw new RuntimeException(
          "[%s] %s - failed".formatted(PG_PROVIDER, "Retrieve Payment Method"));
    }
  }
}
