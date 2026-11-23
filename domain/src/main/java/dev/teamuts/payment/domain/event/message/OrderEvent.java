package dev.teamuts.payment.domain.event.message;

import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.event.EventMessagePublishable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

public class OrderEvent {
  @Builder(access = AccessLevel.PRIVATE)
  public record PayCustomerCompleted(
      Long orderId,
      Long memberId,
      BigDecimal totalAmount,
      Currency currency,
      List<PaymentCompleted> payments)
      implements EventMessagePublishable {
    public static PayCustomerCompleted of(Order order) {
      return PayCustomerCompleted.builder()
          .orderId(order.getId())
          .memberId(order.getMemberId())
          .totalAmount(order.getAmount().getAmount())
          .currency(order.getAmount().getCurrency())
          .payments(order.mapPayments(PaymentCompleted::of))
          .build();
    }

    private record PaymentCompleted(
        Long id,
        PaymentType paymentType,
        BigDecimal payAmount,
        BigDecimal pointAmount,
        List<PaymentTransactionCompleted> transactions) {
      public static PaymentCompleted of(Payment payment) {
        return new PaymentCompleted(
            payment.getId(),
            payment.getPaymentType(),
            payment.getPayAmount().getAmount(),
            payment.getPointAmount().getAmount(),
            payment.mapTransactions(PaymentTransactionCompleted::of));
      }
    }

    private record PaymentTransactionCompleted(
        Long id,
        BigDecimal amount,
        PaySequenceType sequenceType,
        PgProviderType pgProvider,
        PaymentMethodType payMethod) {
      public static PaymentTransactionCompleted of(PaymentTransaction transaction) {
        return new PaymentTransactionCompleted(
            transaction.getId(),
            transaction.getAmount().getAmount(),
            transaction.getSequenceType(),
            transaction.getPgProvider(),
            transaction.getPayMethodType());
      }
    }
  }
}
