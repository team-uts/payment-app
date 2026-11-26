package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.error.PaymentErrorCode;
import dev.teamuts.payment.domain.payment.error.PaymentException;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import dev.teamuts.shared.domain.model.Money;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@DomainModel
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class Payment extends BaseIdentifiableModel<Long> {
  private Long orderId;
  private Long memberId;
  private AppServiceType serviceType;
  private PaymentType paymentType;
  private PaymentStatus status;
  private Money payAmount;
  private Money pointAmount;
  private Long payMethodId;
  private PaymentMethodType payMethodType;
  private PgProviderType pgProvider;

  @Getter(AccessLevel.NONE)
  private OutboxEvent outboxEvent;

  @Getter(AccessLevel.NONE)
  private PaymentTransactions transactions;

  public static Payment processing(
      Order order, PaymentMethod paymentMethod, Money pointAmount, PaymentType paymentType) {
    // TODO: need refactoring
    Assert.notNull(order, "order cannot be null");
    Assert.notNull(paymentMethod, "paymentMethod cannot be null");

    if (order.hasLessAmountThan(pointAmount)) {
      throw PaymentException.of(PaymentErrorCode.CANNOT_BE_GENERATED, order.getExtOrderNum());
    }

    return Payment.builder()
        .orderId(order.getId())
        .memberId(order.getMemberId())
        .serviceType(order.getServiceType())
        .paymentType(paymentType)
        .status(PaymentStatus.PROCESSING)
        .payAmount(order.getRemainingAmount(pointAmount))
        .pointAmount(pointAmount)
        .payMethodId(paymentMethod.getId())
        .payMethodType(paymentMethod.getMethodType())
        .pgProvider(paymentMethod.getPgProvider())
        .build();
  }

  public static Payment processingOnlyPoint(
      Order order, Money pointAmount, PaymentType paymentType) {
    // TODO: need refactoring
    Assert.notNull(order, "order cannot be null");
    Assert.notNull(pointAmount, "pointAmount cannot be null");

    if (order.hasNotEqualAmount(pointAmount) && pointAmount.isGreaterThanZero()) {
      throw PaymentException.of(
          PaymentErrorCode.ONLY_FOR_POINT_CANNOT_BE_GENERATED, order.getExtOrderNum());
    }

    return Payment.builder()
        .orderId(order.getId())
        .memberId(order.getMemberId())
        .serviceType(order.getServiceType())
        .paymentType(paymentType)
        .status(PaymentStatus.PROCESSING)
        .payAmount(Money.zero(order.getAmount().getCurrency()))
        .pointAmount(pointAmount)
        .payMethodId(null)
        .payMethodType(PaymentMethodType.POINT)
        .pgProvider(PgProviderType.NONE)
        .build();
  }

  public static Payment fromDatabase(
      Long id,
      Long orderId,
      Long memberId,
      AppServiceType serviceType,
      PaymentType paymentType,
      PaymentStatus status,
      PgProviderType pgProvider,
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
            .paymentType(paymentType)
            .status(status)
            .pgProvider(pgProvider)
            .payAmount(Money.of(payAmount, currency))
            .pointAmount(Money.of(pointAmount, currency))
            .payMethodId(payMethodId)
            .payMethodType(payMethodType)
            .build();

    payment.setId(id);

    return payment;
  }

  public static Payment fromDatabaseWithAllAssociations(
      Long id,
      Long orderId,
      Long memberId,
      AppServiceType serviceType,
      PaymentType paymentType,
      PaymentStatus status,
      PgProviderType pgProvider,
      BigDecimal payAmount,
      BigDecimal pointAmount,
      Currency currency,
      Long payMethodId,
      PaymentMethodType payMethodType,
      OutboxEvent outboxEvent,
      List<PaymentTransaction> transactions) {
    Payment payment =
        Payment.builder()
            .orderId(orderId)
            .memberId(memberId)
            .serviceType(serviceType)
            .paymentType(paymentType)
            .status(status)
            .pgProvider(pgProvider)
            .payAmount(Money.of(payAmount, currency))
            .pointAmount(Money.of(pointAmount, currency))
            .payMethodId(payMethodId)
            .payMethodType(payMethodType)
            .outboxEvent(outboxEvent)
            .transactions(PaymentTransactions.ofOrEmpty(transactions))
            .build();

    payment.setId(id);

    return payment;
  }

  public void checkProcessingStatus() {
    if (this.status != PaymentStatus.PROCESSING) {
      throw PaymentException.of(PaymentErrorCode.NOT_PROCESSING_STATUS, this.getId());
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

  public boolean hasNoPayTransactions() {
    return this.transactions == null || this.transactions.isEmpty();
  }

  /** {@link PaymentTransactions} status > {@link PaymentStatus} status */
  public void integrateTransactions(PaymentTransactions transactions) {
    this.status = transactions.resolvePaymentStatus();
    this.transactions = transactions;
  }

  public void checkValidTransactions() {
    if (hasNoPayTransactions() || this.status != transactions.resolvePaymentStatus()) {
      throw PaymentException.of(PaymentErrorCode.INVALID_TRANSACTIONS, this.getId());
    }
  }

  public void checkValidOutboxEvent() {
    if (this.outboxEvent == null
        || this.outboxEvent.isNotPersisted()
        || this.outboxEvent.isNotPaymentEvent()) {
      throw PaymentException.of(PaymentErrorCode.INVALID_OUTBOX_EVENT, this.getId());
    }
  }

  public boolean isProcessing() {
    return status == PaymentStatus.PROCESSING;
  }

  public boolean isNotCompleted() {
    return status != PaymentStatus.COMPLETED;
  }

  public boolean isFinalized() {
    return this.status.isFinal();
  }

  public boolean cannotProcessPayTransaction() {
    return hasNoPayAmount() && hasNoPointAmount();
  }

  public Currency getCurrency() {
    if (this.payAmount.hasNotSameCurrency(this.pointAmount)) {
      throw PaymentException.of(PaymentErrorCode.HAS_NOT_SAME_CURRENCY, this.getId());
    }

    return this.payAmount.getCurrency();
  }

  /** payment -> outbox event link */
  public void linkOutboxEvent(OutboxEvent event) {
    checkPersisted();

    // if it is not completed, cannot link outbox event to prevent publication later.
    if (this.isNotCompleted()) {
      throw PaymentException.of(PaymentErrorCode.CANNOT_UPDATE_OUTBOX_EVENT, this.getId());
    }

    this.outboxEvent = event;

    // check after linking
    checkValidOutboxEvent();
  }

  public Long getOutboxEventId() {
    return this.outboxEvent.getId();
  }

  public OutboxEvent getOutboxEvent() {
    return this.outboxEvent.copy();
  }
}
