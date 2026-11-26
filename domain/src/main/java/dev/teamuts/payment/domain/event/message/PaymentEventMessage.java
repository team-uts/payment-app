package dev.teamuts.payment.domain.event.message;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.event.BaseEventMessage;
import java.math.BigDecimal;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class PaymentEventMessage extends BaseEventMessage {
  private Long paymentId;
  private Long memberId;
  private Long orderId;
  private String extOrderNum;
  private AppServiceType serviceType;
  private PaymentType paymentType;
  private PaymentStatus status;
  private BigDecimal payAmount;
  private BigDecimal pointAmount;
  private Currency currency;
  private Long payMethodId;
  private PaymentMethodType payMethodType;
  private PgProviderType pgProvider;

  public static PaymentEventMessage of(Order order, Payment payment) {
    return PaymentEventMessage.builder()
        .paymentId(payment.getId())
        .orderId(order.getId())
        .extOrderNum(order.getExtOrderNum())
        .memberId(order.getMemberId())
        .serviceType(order.getServiceType())
        .paymentType(payment.getPaymentType())
        .status(payment.getStatus())
        .payAmount(payment.getPayAmount().getAmount())
        .pointAmount(payment.getPointAmount().getAmount())
        .currency(payment.getCurrency())
        .payMethodId(payment.getPayMethodId())
        .payMethodType(payment.getPayMethodType())
        .pgProvider(payment.getPgProvider())
        .build();
  }
}
