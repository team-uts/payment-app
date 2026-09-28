package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventStatus;
import dev.teamuts.payment.domain.pg.port.infra.PGWebhookEventStatusMappable;
import dev.teamuts.payment.domain.pg.port.infra.PGWebhookPayloadParsable;
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
  private String pgDetailedMessage;
  private PGWebhookEventStatus status;

  public static ExtPGWebhookEventDto of(
      PGProviderType pgProvider,
      PGRequestType requestType,
      PGWebhookPayloadParsable payload,
      PGWebhookEventStatusMappable statusMappable) {
    return ExtPGWebhookEventDto.builder()
        .pgProvider(pgProvider)
        .requestType(requestType)
        .memberId(payload.getMemberId())
        .pgRequestId(payload.getPGRequestId()) // e.g., SetupIntent, PaymentIntent ID
        .pgProviderToken(payload.getPGProviderToken()) // e.g., Stripe PaymentMethod ID
        .pgOperationName(statusMappable.getPGOperationName())
        .pgDetailedMessage(
            statusMappable.getPGDetailedMessage()) // e.g., setup_intent.succeeded, ...
        .status(statusMappable.getWebhookEventStatus())
        .build();
  }

  public boolean isSucceeded() {
    return this.status == PGWebhookEventStatus.SUCCEEDED;
  }
}
