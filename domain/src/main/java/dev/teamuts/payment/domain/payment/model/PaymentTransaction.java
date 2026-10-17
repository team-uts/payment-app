package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.ExtPgPaymentTransaction;
import dev.teamuts.payment.shared.utils.StringUtils;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class PaymentTransaction {
  private Long id;
  private Long memberId;
  private Long paymentId;
  private Long orderId;
  private Long payMethodId;
  private PaymentMethodType payMethodType;
  private AppServiceType serviceType;

  @Getter(AccessLevel.NONE)
  private Money amount;

  private Currency currency;
  private PGProviderType pgProvider;
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

  public static PaymentTransaction initPay(Payment payment) {
    if (payment.hasNoPayAmount()) {
      throw new RuntimeException(
          "This Payment (id: %d) is invalid for payment transaction".formatted(payment.getId()));
    }

    return PaymentTransaction.builder()
        .memberId(payment.getMemberId())
        .paymentId(payment.getId())
        .orderId(payment.getOrderId())
        .payMethodId(payment.getPayMethodId())
        .payMethodType(payment.getPayMethodType())
        .serviceType(payment.getServiceType())
        .amount(payment.getPayAmountMoney())
        .currency(payment.getCurrency())
        .pgProvider(payment.getPgProvider())
        .status(PaymentTransactionStatus.INIT)
        .sequenceType(PaySequenceType.PG_PAY)
        .build();
  }

  public static PaymentTransaction initPoint(Payment payment) {
    if (payment.hasNoPointAmount()) {
      throw new RuntimeException(
          "This Payment (id: %d) is invalid for point transaction".formatted(payment.getId()));
    }

    return PaymentTransaction.builder()
        .memberId(payment.getMemberId())
        .paymentId(payment.getId())
        .orderId(payment.getOrderId())
        .payMethodId(payment.getPayMethodId())
        .payMethodType(payment.getPayMethodType())
        .serviceType(payment.getServiceType())
        .amount(payment.getPointAmountMoney())
        .currency(payment.getCurrency())
        .pgProvider(PGProviderType.NONE)
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
      BigDecimal amount,
      Currency currency,
      PGProviderType pgProvider,
      String pgTransactionId,
      String pgResponseCode,
      String pgErrorCode,
      String pgErrorMessage,
      String detailMessage,
      PaymentTransactionStatus status,
      Integer sequence) {
    return PaymentTransaction.builder()
        .id(id)
        .memberId(memberId)
        .paymentId(paymentId)
        .orderId(orderId)
        .payMethodId(payMethodId)
        .payMethodType(payMethodType)
        .serviceType(serviceType)
        .amount(Money.of(amount, currency))
        .currency(currency)
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

  public BigDecimal getAmount() {
    return amount.getAmount();
  }

  public boolean cannotContinueProcessingPgPay() {
    return !PaymentTransactionStatus.PG_PAY_PROCESSABLE_STATUSES.contains(this.status);
  }

  public boolean requiresPgAssociation() {
    return this.status == PaymentTransactionStatus.INIT && this.pgTransactionId == null;
  }

  public boolean isAlreadyAssociatedWithPg() {
    return this.status != PaymentTransactionStatus.INIT && this.pgTransactionId != null;
  }

  public boolean canBeConfirmed() {
    return this.status == PaymentTransactionStatus.READY;
  }
}
