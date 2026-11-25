package dev.teamuts.payment.domain.order.model;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.error.OrderErrorCode;
import dev.teamuts.payment.domain.order.error.OrderException;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import dev.teamuts.shared.domain.model.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
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
  private Money amount;
  private OrderStatus status;

  @Getter(AccessLevel.NONE)
  private Payments payments;

  public static Order init(Long memberId, PayOrderCommand command) {
    return Order.builder()
        .memberId(memberId)
        .extOrderNum(command.extOrderNum())
        .serviceType(command.serviceType())
        .amount(command.totalAmount())
        .status(OrderStatus.INIT)
        .payments(Payments.empty())
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
            .payments(Payments.empty())
            .build();

    order.setId(id);

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

  public boolean cannotPublishPayEvent() {
    return this.status != OrderStatus.CONFIRMED;
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
          this.getId(),
          this.amount.getAmount(),
          amount.getAmount());
    }

    return this.amount.minus(amount);
  }

  public void paymentPending() {
    this.checkPersisted();

    if (this.status != OrderStatus.INIT) {
      throw OrderException.of(OrderErrorCode.STATUS_MUST_BE_INIT, this.getId(), this.status);
    }

    this.status = OrderStatus.PAYMENT_PENDING;
  }

  public void applyNewPayments(Payments payments) {
    this.checkPersisted();

    // change order status based on the payments
    this.status = payments.resolveOrderStatus();
    // insert the payment into order.
    insertPaymentsIfEmpty(payments, false);
  }

  public void insertPaymentsIfEmpty(Payments payments, boolean passCheck) {
    this.checkPersisted();

    if (this.payments.isNotEmpty()) {
      return;
    }

    if (!passCheck && payments.resolveOrderStatus() != this.status) {
      throw OrderException.of(
          OrderErrorCode.PAYMENTS_STATUS_NOT_MATCH_ORDER_STATUS,
          this.getId(),
          this.status,
          payments.resolveOrderStatus());
    }

    this.payments = payments;
  }

  public boolean hasNoPayments() {
    return this.payments.isEmpty();
  }

  public <R> List<R> mapPayments(Function<Payment, R> mapper) {
    if (hasNoPayments()) {
      return List.of();
    }

    return this.payments.mapToList(mapper);
  }

  public void processAllPaymentOutboxEvents(Consumer<OutboxEvent> consumer) {
    this.checkPersisted();

    // check all the outbox events
    payments.forEach(Payment::checkValidOutboxEvent);

    payments.processOutboxEventForEachPayment(consumer);
  }
}
