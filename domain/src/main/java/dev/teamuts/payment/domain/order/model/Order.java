package dev.teamuts.payment.domain.order.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class Order {
  private Long id;
  private String extOrderNum;
  private AppServiceType serviceType;
  private Long memberId;

  @Getter(AccessLevel.NONE)
  private Money amount;

  private OrderStatus status;

  public static Order initPayment(Long memberId, PayOrderCommand command) {
    return Order.builder()
        .memberId(memberId)
        .extOrderNum(command.extOrderNum())
        .serviceType(command.serviceType())
        .amount(command.totalAmount())
        .status(OrderStatus.INIT)
        .build();
  }

  public static Order fromDatabase(
      Long id,
      String extOrderNum,
      AppServiceType serviceType,
      Long memberId,
      BigDecimal amount,
      Currency currency,
      OrderStatus status) {
    return Order.builder()
        .id(id)
        .extOrderNum(extOrderNum)
        .serviceType(serviceType)
        .memberId(memberId)
        .amount(Money.of(amount, currency))
        .status(status)
        .build();
  }

  public boolean isTheSameMember(Long memberId) {
    return this.memberId.equals(memberId);
  }

  public boolean isNotTheSameMember(Long memberId) {
    return !isTheSameMember(memberId);
  }

  public boolean isPaymentPending() {
    return this.status == OrderStatus.PAYMENT_PENDING;
  }

  public boolean hasLessAmountThan(Money amount) {
    return this.amount.isLessThan(amount);
  }

  public boolean hasNotEqualAmount(Money amount) {
    return !this.amount.equals(amount);
  }

  public Money getRemainingAmount(Money amount) {
    if (hasLessAmountThan(amount)) {
      throw new RuntimeException(
          "Order (id: %d) have no remaining amount (amount: %.2f < minus amount: %.2f)"
              .formatted(this.id, this.amount.getAmount(), amount.getAmount()));
    }

    return this.amount.minus(amount);
  }

  public BigDecimal getAmount() {
    return amount.getAmount();
  }

  public Currency getCurrency() {
    return amount.getCurrency();
  }

  public void paymentPending() {
    // TODO: check the persistence status
    if (this.status != OrderStatus.INIT) {
      throw new RuntimeException(
          "Order (id: %d) status must be INIT to change to PAYMENT_PENDING, but current status is %s"
              .formatted(this.id, this.status));
    }

    this.status = OrderStatus.PAYMENT_PENDING;
  }

  public void updateStatus(OrderStatus newStatus) {
    this.status = newStatus;
  }
}
