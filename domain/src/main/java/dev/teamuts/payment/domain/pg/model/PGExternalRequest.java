package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestStatus;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class PGExternalRequest {
  private Long id;
  private String pgRequestId;
  private Long memberId;
  private Long
      sourceRequestId; // the ID of the original request that triggered this external request
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

  public static PGExternalRequest initWebhookEvent(ExtPGWebhookEventDto webhookEvent) {
    return PGExternalRequest.builder()
        .pgRequestId(webhookEvent.getPgRequestId())
        .memberId(webhookEvent.getMemberId())
        .pgProvider(webhookEvent.getPgProvider())
        .requestType(webhookEvent.getRequestType())
        .extOperation(webhookEvent.getPgOperationName())
        .extDetailedMessage(webhookEvent.getPgDetailedMessage())
        .status(PGRequestStatus.INIT)
        .build();
  }

  public static PGExternalRequest fromDatabase(
      Long id,
      String pgRequestId,
      Long memberId,
      Long sourceRequestId,
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
        .sourceRequestId(sourceRequestId)
        .pgProvider(pgProvider)
        .requestType(requestType)
        .extOperation(extOperation)
        .extDetailedMessage(extDetailedMessage)
        .providerSecret(providerSecret)
        .status(status)
        .build();
  }

  public boolean isSameRequestTypeWith(PGExternalRequest other) {
    return this.requestType == other.requestType;
  }

  // TODO: check if this model is persistent in the database.
  public void completed() {
    this.status = PGRequestStatus.COMPLETED;
  }

  public void updateStatusBasedOnWebhookProcessingResult(
      PGWebhookEventProcessResultType processResultType) {
    this.status = processResultType.getPGRequestStatus();
  }

  // TODO: check if these models are persistent in the database.
  public void associateWithSource(PGExternalRequest source) {
    Assert.state(
        this.isSameRequestTypeWith(source),
        () ->
            "Request types must match for association. (target: %s, source: %s)"
                .formatted(this.requestType, source.requestType));

    this.sourceRequestId = source.id;
  }
}
