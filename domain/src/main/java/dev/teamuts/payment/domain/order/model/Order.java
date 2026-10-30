package dev.teamuts.payment.domain.order.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.BaseIdentifiableModel;
import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.exception.OrderErrorCode;
import dev.teamuts.payment.domain.order.exception.OrderException;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class Order extends BaseIdentifiableModel<Long> {
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
    Order order =
        Order.builder()
            .extOrderNum(extOrderNum)
            .serviceType(serviceType)
            .memberId(memberId)
            .amount(Money.of(amount, currency))
            .status(status)
            .build();

    order.id = id;

    return order;
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

  public boolean cannotProcessPayment() {
    return this.status.isFinal();
  }

  public boolean hasLessAmountThan(Money amount) {
    return this.amount.isLessThan(amount);
  }

  public boolean hasNotEqualAmount(Money amount) {
    return !this.amount.equals(amount);
  }

  /**
   * Calculate remaining amount from order's total amount.
   *
   * @param amount (e.g., point amount)
   * @return remaining amount after subtracting the argument amount.
   */
  public Money getRemainingAmount(Money amount) {
    if (hasLessAmountThan(amount)) {
      throw OrderException.of(
          OrderErrorCode.HAVE_NO_REMAINING_AMOUNT,
          this.id,
          this.amount.getAmount(),
          amount.getAmount());
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
    this.checkPersisted();

    if (this.status != OrderStatus.INIT) {
      throw OrderException.of(OrderErrorCode.STATUS_MUST_BE_INIT, this.id, this.status);
    }

    this.status = OrderStatus.PAYMENT_PENDING;
  }

  public void updateStatus(OrderStatus newStatus) {
    this.status = newStatus;
  }
}
