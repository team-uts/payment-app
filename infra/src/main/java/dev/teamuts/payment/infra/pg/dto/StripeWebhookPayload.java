package dev.teamuts.payment.infra.pg.dto;

import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StripeWebhookPayload {
  private Long memberId;
  private String pgOperationId;
  private StripeWebhookEventType eventType;
  private String pgProviderTokenId; // stripe payment_method_id, payment_id, ...
}
