package dev.teamuts.payment.domain.event.message;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.event.BaseEventMessage;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

public class PaymentEventMessage {
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder(access = AccessLevel.PRIVATE)
  public static class PayCustomerCompleted extends BaseEventMessage {
    private Long orderId;
    private Long memberId;
    private Long paymentId;
    private BigDecimal payAmount;
    private BigDecimal pointAmount;
    private Currency currency;
    private PaymentType paymentType;
    private List<PaymentTransactionCompleted> transaction;

    public static PayCustomerCompleted of(Payment payment) {
      return PayCustomerCompleted.builder()
          .orderId(payment.getOrderId())
          .memberId(payment.getMemberId())
          .paymentId(payment.getId())
          .payAmount(payment.getPayAmount().getAmount())
          .pointAmount(payment.getPointAmount().getAmount())
          .currency(payment.getCurrency())
          .paymentType(payment.getPaymentType())
          .transaction(payment.mapTransactions(PaymentTransactionCompleted::of))
          .build();
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
