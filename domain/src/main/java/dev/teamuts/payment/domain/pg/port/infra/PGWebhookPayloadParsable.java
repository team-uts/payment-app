package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.pg.constant.PGWebhookEventStatus;

public interface PGWebhookPayloadParsable {
  Long getMemberId();

  String getPGRequestId();

  String getPGProviderToken();

  String getPGOperationName();

  String getPGDetailedMessage();

  PGWebhookEventStatus getWebhookEventStatus();
}
