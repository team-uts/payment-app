package dev.teamuts.payment.infra.pg.constant;

import static dev.teamuts.payment.domain.pg.constant.PGWebhookEventStatus.*;
import static dev.teamuts.payment.infra.pg.constant.ExtPGOperationType.*;

import dev.teamuts.payment.domain.pg.constant.PGWebhookEventStatus;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StripeWebhookEventType {
  SETUP_INTENT_SUCCEEDED(WEBHOOK_SETUP_INTENT, "setup_intent.succeeded", SUCCEEDED),
  SETUP_INTENT_SETUP_FAILED(WEBHOOK_SETUP_INTENT, "setup_intent.setup_failed", FAILED),
  PAYMENT_INTENT_SUCCEEDED(WEBHOOK_PAYMENT_INTENT, "payment_intent.succeeded", SUCCEEDED),
  PAYMENT_INTENT_PAYMENT_FAILED(WEBHOOK_PAYMENT_INTENT, "payment_intent.payment_failed", FAILED);

  private final ExtPGOperationType operationType;
  private final String eventTypeName;
  private final PGWebhookEventStatus status;

  private static final Map<String, StripeWebhookEventType> EVENT_TYPE_NAME_MAP =
      Arrays.stream(values())
          .collect(Collectors.toUnmodifiableMap(StripeWebhookEventType::getEventTypeName, e -> e));

  public static StripeWebhookEventType fromEventTypeName(String eventTypeName) {
    StripeWebhookEventType eventType = EVENT_TYPE_NAME_MAP.get(eventTypeName);

    if (eventType == null) {
      throw new IllegalArgumentException("Unknown Stripe webhook event type: " + eventTypeName);
    }

    return eventType;
  }

  public String getPGOperationName() {
    return this.getOperationType().name();
  }
}
