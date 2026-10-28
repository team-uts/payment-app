package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.BaseIdentifiableModel;
import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.exception.PaymentErrorCode;
import dev.teamuts.payment.domain.payment.exception.PaymentException;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class Payment extends BaseIdentifiableModel<Long> {
  private Long orderId;
  private Long memberId;
  private AppServiceType serviceType;
  private PaymentStatus status;

  @Getter(AccessLevel.NONE)
  private Money payAmount;

  @Getter(AccessLevel.NONE)
  private Money pointAmount;

  private Long payMethodId;
  private PaymentMethodType payMethodType;
  private PGProviderType pgProvider;

  @Getter(AccessLevel.NONE)
  private PaymentTransactions transactions;

  public static Payment processing(Order order, PaymentMethod paymentMethod, Money pointAmount) {
    if (order.hasLessAmountThan(pointAmount)) {
      throw PaymentException.of(PaymentErrorCode.CANNOT_BE_GENERATED, order.getExtOrderNum());
    }

    return Payment.builder()
        .orderId(order.getId())
        .memberId(order.getMemberId())
        .serviceType(order.getServiceType())
        .status(PaymentStatus.PROCESSING)
        .payAmount(order.getRemainingAmount(pointAmount))
        .pointAmount(pointAmount)
        .payMethodId(paymentMethod.getId())
        .payMethodType(paymentMethod.getMethodType())
        .pgProvider(paymentMethod.getPgProvider())
        .build();
  }

  public static Payment processingOnlyPoint(Order order, Money pointAmount) {
    if (order.hasNotEqualAmount(pointAmount) && pointAmount.isGreaterThanZero()) {
      throw PaymentException.of(
          PaymentErrorCode.ONLY_FOR_POINT_CANNOT_BE_GENERATED, order.getExtOrderNum());
    }

    return Payment.builder()
        .orderId(order.getId())
        .memberId(order.getMemberId())
        .serviceType(order.getServiceType())
        .status(PaymentStatus.PROCESSING)
        .payAmount(Money.zero(order.getCurrency()))
        .pointAmount(pointAmount)
        .payMethodId(null)
        .payMethodType(PaymentMethodType.POINT)
        .pgProvider(PGProviderType.NONE)
        .build();
  }

  public static Payment fromDatabase(
      Long id,
      Long orderId,
      Long memberId,
      AppServiceType serviceType,
      PaymentStatus status,
      PGProviderType pgProvider,
      BigDecimal payAmount,
      BigDecimal pointAmount,
      Currency currency,
      Long payMethodId,
      PaymentMethodType payMethodType) {
    Payment payment =
        Payment.builder()
            .orderId(orderId)
            .memberId(memberId)
            .serviceType(serviceType)
            .status(status)
            .pgProvider(pgProvider)
            .payAmount(Money.of(payAmount, currency))
            .pointAmount(Money.of(pointAmount, currency))
            .payMethodId(payMethodId)
            .payMethodType(payMethodType)
            .build();

    payment.id = id;

    return payment;
  }

  public Money getPayAmountMoney() {
    return payAmount.copy();
  }

  public Money getPointAmountMoney() {
    return pointAmount.copy();
  }

  public BigDecimal getPayAmount() {
    return payAmount.getAmount();
  }

  public BigDecimal getPointAmount() {
    return pointAmount.getAmount();
  }

  public void checkProcessingStatus() {
    if (this.status != PaymentStatus.PROCESSING) {
      throw PaymentException.of(PaymentErrorCode.NOT_PROCESSING_STATUS, this.id);
    }
  }

  public boolean hasPayAmount() {
    return payAmount.isGreaterThanZero();
  }

  public boolean hasPointAmount() {
    return pointAmount.isGreaterThanZero();
  }

  public boolean hasNoPayAmount() {
    return !hasPayAmount();
  }

  public boolean hasNoPointAmount() {
    return !hasPointAmount();
  }

  public void integrateTransactions(PaymentTransactions transactions) {
    this.status = transactions.resolvePaymentStatus();
    this.transactions = transactions;
  }

  public boolean isProcessing() {
    return status == PaymentStatus.PROCESSING;
  }

  public boolean cannotProcessPayTransaction() {
    return this.status.isFinal() || (hasNoPayAmount() && hasNoPointAmount());
  }

  public OrderStatus resolveOrderStatus() {
    return status.getOrderStatus();
  }

  public Currency getCurrency() {
    if (this.payAmount.hasNotSameCurrency(this.pointAmount)) {
      throw PaymentException.of(PaymentErrorCode.HAS_NOT_SAME_CURRENCY, this.id);
    }

    return this.payAmount.getCurrency();
  }
}
