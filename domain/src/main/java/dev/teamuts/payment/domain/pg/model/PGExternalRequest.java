package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.BaseIdentifiableModel;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestStatus;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGWebhookEventDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@DomainModel
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class PGExternalRequest extends BaseIdentifiableModel<Long> {
  private String pgRequestId;
  private Long memberId;
  // the ID of the original request that triggered this external request
  private Long sourceRequestId;
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
    PGExternalRequest pgRequest =
        PGExternalRequest.builder()
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

    pgRequest.id = id;

    return pgRequest;
  }

  public boolean isSameRequestTypeWith(PGExternalRequest other) {
    return this.requestType == other.requestType;
  }

  public boolean isCompleted() {
    return this.status == PGRequestStatus.COMPLETED;
  }

  public void completed() {
    this.checkPersisted();

    this.status = PGRequestStatus.COMPLETED;
  }

  public void updateStatusBasedOnWebhookProcessingResult(
      PGWebhookEventProcessResultType processResultType) {
    this.status = processResultType.getPGRequestStatus();
  }

  public void associateWithSource(PGExternalRequest source) {
    source.checkPersisted();
    this.checkPersisted();

    Assert.state(
        this.isSameRequestTypeWith(source),
        () ->
            "Request types must match for association. (target: %s, source: %s)"
                .formatted(this.requestType, source.requestType));

    this.sourceRequestId = source.id;
  }
}
