package dev.teamuts.payment.infra.pg.adapter;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.PGExtOperationInfo.WebhookEventInfo;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPGServicePort;
import dev.teamuts.payment.infra.common.annotation.PGAdapter;
import dev.teamuts.payment.infra.pg.constant.ExtPGOperationType;
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

  @Override
  public ExtPGAccountDto createNewAccount(Long memberId, String email) {
    ExtPGOperationType operationType = ExtPGOperationType.CREATE_CUSTOMER;

    // AccountV2 is not supported by Stripe for sandbox.
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

  @Override
  public WebhookEventInfo parseWebhookEvent(
      PGRequestType requestType, String payload, String secret) {
    String endpointSecret = webhookSecretManager.getEndpointSecret(requestType);

    try {
      Event event = stripeClient.constructEvent(payload, secret, endpointSecret);
      StripeWebhookEventType eventType = StripeWebhookEventType.fromEventTypeName(event.getType());

      StripeWebhookPayload stripePayload =
          webhookEventMapperProvider.getInstance(eventType).convert(event);

      return WebhookEventInfo.builder()
          .pgProvider(PG_PROVIDER)
          .requestType(requestType)
          .memberId(stripePayload.getMemberId())
          .pgRequestId(stripePayload.getPgOperationId())
          .pgProviderToken(stripePayload.getPgProviderTokenId())
          .build();
    } catch (SignatureVerificationException e) {
      log.error(e.getMessage());

      throw new RuntimeException("[%s] Webhook verification failed".formatted(PG_PROVIDER));
    }
  }
}
