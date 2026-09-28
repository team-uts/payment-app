package dev.teamuts.payment.domain.pg.port.infra;

public interface PGWebhookPayloadParsable {
  Long getMemberId();

  String getPGRequestId();

  String getPGProviderToken();
}
