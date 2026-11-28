package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.error.PaymentException;
import dev.teamuts.payment.domain.payment.error.PaymentTransactionErrorCode;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.model.sub.ExtPgPaymentTransaction;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.domain.constant.ModelSourceType;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import dev.teamuts.shared.domain.model.Money;
import dev.teamuts.shared.utils.StringUtils;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Getter
public class PaymentTransaction extends BaseIdentifiableModel<Long> {
  private Long memberId;
  private Long paymentId;
  private Long orderId;
  private Long payMethodId;
  private PaymentMethodType payMethodType;
  private AppServiceType serviceType;
  private PaymentType paymentType;
  private Money amount;
  private PgProviderType pgProvider;
  private String pgTransactionId;
  private String pgResponseCode;
  private String pgErrorCode;
  private String pgErrorMessage;
  private String detailMessage;
  private PaymentTransactionStatus status;
  private PaySequenceType sequenceType;

  public static final int MAX_RESPONSE_CODE_LENGTH = 255;
  public static final int MAX_ERROR_CODE_LENGTH = 255;
  public static final int MAX_ERROR_MSG_LENGTH = 255;
  public static final int MAX_DETAIL_MSG_LENGTH = 255;

  @Builder(access = AccessLevel.PRIVATE, toBuilder = true)
  private PaymentTransaction(
      @Builder.ObtainVia(method = "getId") Long id,
      @Builder.ObtainVia(method = "getSourceType") ModelSourceType sourceType,
      Long memberId,
      Long paymentId,
      Long orderId,
      Long payMethodId,
      PaymentMethodType payMethodType,
      AppServiceType serviceType,
      PaymentType paymentType,
      Money amount,
      PgProviderType pgProvider,
      String pgTransactionId,
      String pgResponseCode,
      String pgErrorCode,
      String pgErrorMessage,
      String detailMessage,
      PaymentTransactionStatus status,
      PaySequenceType sequenceType) {
    super(id, sourceType);
    this.memberId = memberId;
    this.paymentId = paymentId;
    this.orderId = orderId;
    this.payMethodId = payMethodId;
    this.payMethodType = payMethodType;
    this.serviceType = serviceType;
    this.paymentType = paymentType;
    this.amount = amount;
    this.pgProvider = pgProvider;
    this.pgTransactionId = pgTransactionId;
    this.pgResponseCode = pgResponseCode;
    this.pgErrorCode = pgErrorCode;
    this.pgErrorMessage = pgErrorMessage;
    this.detailMessage = detailMessage;
    this.status = status;
    this.sequenceType = sequenceType;
  }

  public static PaymentTransaction initPay(Payment payment) {
    if (payment.hasNoPayAmount()) {
      throw PaymentException.of(
          PaymentTransactionErrorCode.CANNOT_GENERATE_WITH_NO_PAY_AMOUNT, payment.getId());
    }

    return PaymentTransaction.builder()
        .sourceType(ModelSourceType.LOGIC)
        .memberId(payment.getMemberId())
        .paymentId(payment.getId())
        .orderId(payment.getOrderId())
        .payMethodId(payment.getPayMethodId())
        .payMethodType(payment.getPayMethodType())
        .serviceType(payment.getServiceType())
        .paymentType(payment.getPaymentType())
        .amount(payment.getPayAmount())
        .pgProvider(payment.getPgProvider())
        .status(PaymentTransactionStatus.INIT)
        .sequenceType(PaySequenceType.PG_PAY)
        .build();
  }

  public static PaymentTransaction initPoint(Payment payment) {
    if (payment.hasNoPointAmount()) {
      throw PaymentException.of(
          PaymentTransactionErrorCode.CANNOT_GENERATE_WITH_NO_POINT_AMOUNT, payment.getId());
    }

    return PaymentTransaction.builder()
        .sourceType(ModelSourceType.LOGIC)
        .memberId(payment.getMemberId())
        .paymentId(payment.getId())
        .orderId(payment.getOrderId())
        .payMethodId(payment.getPayMethodId())
        .payMethodType(payment.getPayMethodType())
        .serviceType(payment.getServiceType())
        .paymentType(payment.getPaymentType())
        .amount(payment.getPointAmount())
        .pgProvider(PgProviderType.NONE)
        .status(PaymentTransactionStatus.INIT)
        .sequenceType(PaySequenceType.POINT)
        .build();
  }

  public static PaymentTransaction fromDatabase(
      Long id,
      Long memberId,
      Long paymentId,
      Long orderId,
      Long payMethodId,
      PaymentMethodType payMethodType,
      AppServiceType serviceType,
      PaymentType paymentType,
      BigDecimal amount,
      Currency currency,
      PgProviderType pgProvider,
      String pgTransactionId,
      String pgResponseCode,
      String pgErrorCode,
      String pgErrorMessage,
      String detailMessage,
      PaymentTransactionStatus status,
      Integer sequence) {
    return PaymentTransaction.builder()
        .id(id)
        .sourceType(ModelSourceType.DATABASE)
        .memberId(memberId)
        .paymentId(paymentId)
        .orderId(orderId)
        .payMethodId(payMethodId)
        .payMethodType(payMethodType)
        .serviceType(serviceType)
        .paymentType(paymentType)
        .amount(Money.of(amount, currency))
        .pgProvider(pgProvider)
        .pgTransactionId(pgTransactionId)
        .pgResponseCode(pgResponseCode)
        .pgErrorCode(pgErrorCode)
        .pgErrorMessage(pgErrorMessage)
        .detailMessage(detailMessage)
        .status(status)
        .sequenceType(PaySequenceType.of(sequence))
        .build();
  }

  public boolean isInit() {
    return this.status == PaymentTransactionStatus.INIT;
  }

  public boolean isFailed() {
    return this.status == PaymentTransactionStatus.FAILED;
  }

  public boolean isCompleted() {
    return this.status == PaymentTransactionStatus.COMPLETED;
  }

  public boolean isNotCompleted() {
    return !isCompleted();
  }

  public boolean isNotProcessable() {
    return this.status.isFinal();
  }

  public void updatePGTransactionInfo(ExtPgPaymentTransaction pgResult) {
    this.pgTransactionId = pgResult.getPgTransactionId();
    this.pgResponseCode =
        StringUtils.truncate(pgResult.getPgResponseCode(), MAX_RESPONSE_CODE_LENGTH);
    this.pgErrorCode = StringUtils.truncate(pgResult.getPgErrCode(), MAX_ERROR_CODE_LENGTH);
    this.pgErrorMessage = StringUtils.truncate(pgResult.getPgErrMessage(), MAX_ERROR_MSG_LENGTH);
    this.detailMessage = StringUtils.truncate(pgResult.getPgOperationName(), MAX_DETAIL_MSG_LENGTH);

    this.status = pgResult.getDomainStatus();
  }

  public void updateWithAppException(Exception e) {
    this.detailMessage =
        StringUtils.truncate(
            "[%s] %s".formatted(e.getClass().getSimpleName(), e.getMessage()),
            MAX_DETAIL_MSG_LENGTH);
  }

  public boolean cannotContinueProcessingPgPay() {
    return !PaymentTransactionStatus.PG_PAY_PROCESSABLE_STATUSES.contains(this.status);
  }

  public boolean needsPgAssociation() {
    return this.status == PaymentTransactionStatus.INIT && this.pgTransactionId == null;
  }

  public boolean isAlreadyAssociatedWithPg() {
    return this.status != PaymentTransactionStatus.INIT && this.pgTransactionId != null;
  }

  public boolean canBeConfirmed() {
    return this.status == PaymentTransactionStatus.READY;
  }
}
