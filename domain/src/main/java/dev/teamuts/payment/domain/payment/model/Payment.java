package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class Payment {
  private Long id;
  private Long orderId;
  private Long memberId;
  private AppServiceType serviceType;
  private PaymentStatus status;

  @Getter(AccessLevel.NONE)
  private Money payAmount;

  @Getter(AccessLevel.NONE)
  private Money pointAmount;

  private Currency currency;
  private Long payMethodId;
  private PaymentMethodType payMethodType;

  public static Payment processing(Order order, PaymentMethod paymentMethod, Money pointAmount) {
    if (order.hasLessAmountThan(pointAmount)) {
      throw new RuntimeException(
          "Order (id: %d) amount should be greater than point amount (%.2f)"
              .formatted(order.getId(), pointAmount.getAmount()));
    }

    if (paymentMethod == null) {
      throw new RuntimeException(
          "Payment method is required for processing payment for order (id: %d)"
              .formatted(order.getId()));
    }

    return Payment.builder()
        .orderId(order.getId())
        .memberId(order.getMemberId())
        .serviceType(order.getServiceType())
        .status(PaymentStatus.PROCESSING)
        .payAmount(order.getRemainingAmount(pointAmount))
        .pointAmount(pointAmount)
        .currency(order.getCurrency())
        .payMethodId(paymentMethod.getId())
        .payMethodType(paymentMethod.getMethodType())
        .build();
  }

  public static Payment processingOnlyPoint(Order order, Money pointAmount) {
    if (order.hasNotEqualAmount(pointAmount) && pointAmount.isGreaterThanZero()) {
      throw new RuntimeException(
          "Order (id: %d) amount and point amount (%.2f) must be equal and greater than zero"
              .formatted(order.getId(), pointAmount.getAmount()));
    }

    return Payment.builder()
        .orderId(order.getId())
        .memberId(order.getMemberId())
        .serviceType(order.getServiceType())
        .status(PaymentStatus.PROCESSING)
        .payAmount(Money.zero(order.getCurrency()))
        .pointAmount(pointAmount)
        .currency(order.getCurrency())
        .payMethodType(PaymentMethodType.POINT)
        .build();
  }

  public static Payment fromDatabase(
      Long id,
      Long orderId,
      Long memberId,
      AppServiceType serviceType,
      PaymentStatus status,
      BigDecimal payAmount,
      BigDecimal pointAmount,
      Currency currency,
      Long payMethodId,
      PaymentMethodType payMethodType) {
    return Payment.builder()
        .id(id)
        .orderId(orderId)
        .memberId(memberId)
        .serviceType(serviceType)
        .status(status)
        .payAmount(Money.of(payAmount, currency))
        .pointAmount(Money.of(pointAmount, currency))
        .currency(currency)
        .payMethodId(payMethodId)
        .payMethodType(payMethodType)
        .build();
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

  public void checkProcessing() {
    if (this.status != PaymentStatus.PROCESSING) {
      throw new RuntimeException("Payment (id: %d) is not in PROCESSING status".formatted(this.id));
    }
  }

  public boolean hasPayAmount() {
    return payAmount.isGreaterThanZero();
  }

  public boolean hasPointAmount() {
    return pointAmount.isGreaterThanZero();
  }

  public boolean hasNoPayAmount() {
    return payAmount.isZero();
  }

  public boolean hasNoPointAmount() {
    return pointAmount.isZero();
  }
}
