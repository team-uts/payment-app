package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.shared.domain.model.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequest {
  private Long payMethodId;
  private Money totalAmount;
  private Money pointAmount;
  private PaymentType paymentType;

  public static PaymentRequest customerRequest(PayOrderCommand command) {
    return new PaymentRequest(
        command.payMethodId(),
        command.totalAmount(),
        command.pointAmount(),
        PaymentType.fromCustomerServiceType(command.serviceType()));
  }

  public boolean hasAnyPointUsage() {
    return pointAmount.isGreaterThanZero();
  }

  public boolean hasOnlyPointUsage() {
    return pointAmount.isGreaterThanZero() && totalAmount.isEqual(pointAmount);
  }
}
