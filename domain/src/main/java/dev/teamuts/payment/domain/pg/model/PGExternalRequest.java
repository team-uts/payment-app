package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestStatus;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.PGExtOperationInfo.WebhookEventInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class PGExternalRequest {
  private Long id;
  private String pgRequestId;
  private Long memberId;
  private PGProviderType pgProvider;
  private PGRequestType requestType;
  private String extOperation;
  private String extDetailedMessage;
  private String providerSecret;
  private PGRequestStatus status;

  public static PGExternalRequest initPaymentMethodSetup(
      ExtPGPaymentMethodOperationDto paymentMethodOperation) {
    return PGExternalRequest.builder()
        .pgRequestId(paymentMethodOperation.getPgOperationId())
        .memberId(paymentMethodOperation.getMemberId())
        .pgProvider(paymentMethodOperation.getPgProvider())
        .requestType(PGRequestType.PAYMENT_METHOD_SETUP)
        .extOperation(paymentMethodOperation.getPgOperationName())
        .providerSecret(paymentMethodOperation.getPgProviderSecret())
        .status(PGRequestStatus.INIT)
        .build();
  }

  public static PGExternalRequest completeWebhookEvent(WebhookEventInfo webhookEvent) {
    return PGExternalRequest.builder()
        .pgRequestId(webhookEvent.getPgRequestId())
        .memberId(webhookEvent.getMemberId())
        .pgProvider(webhookEvent.getPgProvider())
        .requestType(webhookEvent.getRequestType())
        .extOperation(webhookEvent.getPgOperationName())
        .extDetailedMessage(webhookEvent.getPgDetailedMessage())
        .status(PGRequestStatus.COMPLETED)
        .build();
  }

  public static PGExternalRequest fromDatabase(
      Long id,
      String pgRequestId,
      Long memberId,
      PGProviderType pgProvider,
      PGRequestType requestType,
      String extOperation,
      String extDetailedMessage,
      String providerSecret,
      PGRequestStatus status) {
    return PGExternalRequest.builder()
        .id(id)
        .pgRequestId(pgRequestId)
        .memberId(memberId)
        .pgProvider(pgProvider)
        .requestType(requestType)
        .extOperation(extOperation)
        .extDetailedMessage(extDetailedMessage)
        .providerSecret(providerSecret)
        .status(status)
        .build();
  }
}
