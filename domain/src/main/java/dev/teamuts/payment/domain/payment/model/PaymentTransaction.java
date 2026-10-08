package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class PaymentTransaction {
  private Long id;
  private Long memberId;
  private Long paymentId;
  private Long orderId;
  private Long payMethodId;
  private PaymentMethodType payMethodType;
  private AppServiceType serviceType;
  private Money amount;
  private Currency currency;
  private PGProviderType pgProvider;
  private String pgTransactionId;
  private String pgResponseMessage;
  private PaymentTransactionStatus status;
  private PaySequenceType sequenceType;

  public static PaymentTransaction initPay(Payment payment) {
    if (payment.hasNoPayAmount()) {
      throw new RuntimeException(
          "This Payment (id: %d) is invalid for payment transaction".formatted(payment.getId()));
    }

    return PaymentTransaction.builder()
        .memberId(payment.getMemberId())
        .paymentId(payment.getId())
        .orderId(payment.getOrderId())
        .payMethodId(payment.getPayMethodId())
        .payMethodType(payment.getPayMethodType())
        .serviceType(payment.getServiceType())
        .amount(payment.getPayAmountMoney())
        .currency(payment.getCurrency())
        .status(PaymentTransactionStatus.INIT)
        .sequenceType(PaySequenceType.PG_PAY)
        .build();
  }

  public static PaymentTransaction initPoint(Payment payment) {
    if (payment.hasNoPointAmount()) {
      throw new RuntimeException(
          "This Payment (id: %d) is invalid for point transaction".formatted(payment.getId()));
    }

    return PaymentTransaction.builder()
        .memberId(payment.getMemberId())
        .paymentId(payment.getId())
        .orderId(payment.getOrderId())
        .payMethodId(payment.getPayMethodId())
        .payMethodType(payment.getPayMethodType())
        .serviceType(payment.getServiceType())
        .amount(payment.getPointAmountMoney())
        .currency(payment.getCurrency())
        .status(PaymentTransactionStatus.INIT)
        .sequenceType(PaySequenceType.POINT)
        .build();
  }

  public static PaymentTransaction fromDatabase(
      Long id,
      Long memberId,
      Long paymentId,
      Long orderId,
      Long payMethodId,
      PaymentMethodType payMethodType,
      AppServiceType serviceType,
      BigDecimal amount,
      Currency currency,
      PGProviderType pgProvider,
      String pgTransactionId,
      String pgResponseMessage,
      PaymentTransactionStatus status,
      Integer sequence) {
    return PaymentTransaction.builder()
        .id(id)
        .memberId(memberId)
        .paymentId(paymentId)
        .orderId(orderId)
        .payMethodId(payMethodId)
        .payMethodType(payMethodType)
        .serviceType(serviceType)
        .amount(Money.of(amount, currency))
        .currency(currency)
        .pgProvider(pgProvider)
        .pgTransactionId(pgTransactionId)
        .pgResponseMessage(pgResponseMessage)
        .status(status)
        .sequenceType(PaySequenceType.of(sequence))
        .build();
  }
}
