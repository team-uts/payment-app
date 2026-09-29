package dev.teamuts.payment.infra.pg.dto;

import dev.teamuts.payment.domain.pg.constant.PGWebhookEventStatus;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.infra.PGWebhookPayloadParsable;
import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StripeWebhookPayload implements PGWebhookPayloadParsable {
  private Long userId;

  /** associated to {@link PGExternalRequest#getPgRequestId()} */
  private String originObjectId;

  private StripeWebhookEventType eventType;

  /** Stripe PaymentMethod ID, Payment ID, ... */
  private String associatedObjectId;

  @Override
  public Long getMemberId() {
    return userId;
  }

  @Override
  public String getPGRequestId() {
    return originObjectId;
  }

  @Override
  public String getPGProviderToken() {
    return associatedObjectId;
  }

  @Override
  public String getPGOperationName() {
    return eventType.getPGOperationName();
  }

  @Override
  public String getPGDetailedMessage() {
    return eventType.getEventTypeName();
  }

  @Override
  public PGWebhookEventStatus getWebhookEventStatus() {
    return eventType.getStatus();
  }
}
