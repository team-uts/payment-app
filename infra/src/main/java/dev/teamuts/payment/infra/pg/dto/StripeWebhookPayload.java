package dev.teamuts.payment.infra.pg.dto;

import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StripeWebhookPayload {
  private Long memberId;

  /** associated to {@link PGExternalRequest#getPgRequestId()} */
  private String pgOperationId;

  private StripeWebhookEventType eventType;

  /** Stripe PaymentMethod ID, Payment ID, ... */
  private String pgProviderTokenId;
}
