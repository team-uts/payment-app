package dev.teamuts.payment.infra.pg.constant;

import static dev.teamuts.payment.domain.pg.constant.PGProviderType.STRIPE;
import static dev.teamuts.payment.domain.pg.constant.PGRequestType.*;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExtPGOperationType {
  CREATE_CUSTOMER(STRIPE, ACCOUNT, "user_id", false),
  CREATE_SETUP_INTENT(STRIPE, PAYMENT_METHOD_SETUP, "user_id", false),
  CREATE_PAYMENT_INTENT(STRIPE, PAYMENT, null, false),
  WEBHOOK_SETUP_INTENT(STRIPE, PAYMENT_METHOD_SETUP, "user_id", true),
  WEBHOOK_PAYMENT_INTENT(STRIPE, PAYMENT, null, true);

  private final PGProviderType pgProvider;
  private final PGRequestType requestType;
  private final String metadataKey;
  private final Boolean webhookOperation;

  // WebhookEvent > BaseOperationType mapping
  private static final Map<ExtPGOperationType, ExtPGOperationType> WEBHOOK_RELATION_MAP =
      Map.of(
          WEBHOOK_SETUP_INTENT, CREATE_SETUP_INTENT,
          WEBHOOK_PAYMENT_INTENT, CREATE_PAYMENT_INTENT);

  public static ExtPGOperationType getBaseOperationTypeFromRequestType(PGRequestType requestType) {
    return WEBHOOK_RELATION_MAP.entrySet().stream()
        .filter(entry -> entry.getKey().getRequestType() == requestType)
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "No base operation type found for request type: " + requestType))
        .getValue();
  }
}
