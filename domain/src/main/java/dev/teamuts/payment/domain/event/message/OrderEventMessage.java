package dev.teamuts.payment.domain.event.message;

import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.event.BaseEventMessage;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;

public class OrderEventMessage {
  @Builder(access = AccessLevel.PRIVATE)
  public static final class PayCustomerOrder extends BaseEventMessage {
    private final BigDecimal payAmount;
    private final Currency currency;
    private final Long payTransactionId;
    private final PaymentType paymentType;

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
