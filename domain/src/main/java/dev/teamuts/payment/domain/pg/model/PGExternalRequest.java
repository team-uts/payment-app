package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.pg.constant.PGRequestStatus;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGWebhookEventDto;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.constant.ModelSourceType;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@DomainModel
@Getter
public class PGExternalRequest extends BaseIdentifiableModel<Long> {
  private String pgRequestId;
  private Long memberId;
  // the ID of the original request that triggered this external request
  private Long sourceRequestId;
  private PgProviderType pgProvider;
  private PGRequestType requestType;
  private String extOperation;
  private String extDetailedMessage;
  private String providerSecret;
  private PGRequestStatus status;

  @Builder(access = AccessLevel.PRIVATE, toBuilder = true)
  private PGExternalRequest(
      @Builder.ObtainVia(method = "getId") Long id,
      @Builder.ObtainVia(method = "getSourceType") ModelSourceType sourceType,
      String pgRequestId,
      Long memberId,
      Long sourceRequestId,
      PgProviderType pgProvider,
      PGRequestType requestType,
      String extOperation,
      String extDetailedMessage,
      String providerSecret,
      PGRequestStatus status) {
    super(id, sourceType);
    this.pgRequestId = pgRequestId;
    this.memberId = memberId;
    this.sourceRequestId = sourceRequestId;
    this.pgProvider = pgProvider;
    this.requestType = requestType;
    this.extOperation = extOperation;
    this.extDetailedMessage = extDetailedMessage;
    this.providerSecret = providerSecret;
    this.status = status;
  }

  public static PGExternalRequest initPaymentMethodSetup(
      ExtPGPaymentMethodOperationDto paymentMethodOperation) {
    return PGExternalRequest.builder()
        .sourceType(ModelSourceType.LOGIC)
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
        .sourceType(ModelSourceType.LOGIC)
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
      PgProviderType pgProvider,
      PGRequestType requestType,
      String extOperation,
      String extDetailedMessage,
      String providerSecret,
      PGRequestStatus status) {
    return PGExternalRequest.builder()
        .id(id)
        .sourceType(ModelSourceType.DATABASE)
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

    this.sourceRequestId = source.getId();
  }
}
