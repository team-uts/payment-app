package dev.teamuts.payment.domain.event.message;

import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.event.EventMessagePublishable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;

public class OrderEventMessage {
  @Builder(access = AccessLevel.PRIVATE)
  public record PayCustomerOrder(
      BigDecimal payAmount, Currency currency, Long payTransactionId, PaymentType paymentType)
      implements EventMessagePublishable {

    public static PayCustomerOrder of(PaymentTransaction transaction) {
      return PayCustomerOrder.builder()
          .payAmount(transaction.getAmount().getAmount())
          .currency(transaction.getAmount().getCurrency())
          .payTransactionId(transaction.getId())
          .paymentType(transaction.getPaymentType())
          .build();
    }
  }
}
