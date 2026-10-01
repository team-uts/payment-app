package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventStatus;
import dev.teamuts.payment.domain.pg.port.infra.spec.PGWebhookPayloadParsable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ExtPGWebhookEventDto {
  private PGProviderType pgProvider;
  private PGRequestType requestType;
  private Long memberId;
  private String pgRequestId;
  private String pgProviderToken; // e.g., PaymentMethod ID in SetupIntent
  private String pgOperationName;
  private String pgBaseOperationName;
  private String pgDetailedMessage;
  private PGWebhookEventStatus status;

  public static ExtPGWebhookEventDto of(
      PGProviderType pgProvider, PGRequestType requestType, PGWebhookPayloadParsable payload) {
    return ExtPGWebhookEventDto.builder()
        .pgProvider(pgProvider)
        .requestType(requestType)
        .memberId(payload.getMemberId())
        .pgRequestId(payload.getPGRequestId()) // e.g., SetupIntent, PaymentIntent ID
        .pgProviderToken(payload.getPGProviderToken()) // e.g., Stripe PaymentMethod ID
        .pgOperationName(payload.getPGOperationName())
        .pgBaseOperationName(payload.getPGBaseOperationName())
        .pgDetailedMessage(payload.getPGDetailedMessage()) // e.g., setup_intent.succeeded, ...
        .status(payload.getWebhookEventStatus())
        .build();
  }

  public boolean isSucceeded() {
    return this.status == PGWebhookEventStatus.SUCCEEDED;
  }
}
