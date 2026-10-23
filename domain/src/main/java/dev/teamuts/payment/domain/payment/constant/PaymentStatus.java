package dev.teamuts.payment.domain.payment.constant;

import dev.teamuts.payment.domain.order.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum PaymentStatus {
  PROCESSING(OrderStatus.PAYMENT_PENDING),
  PENDING(OrderStatus.PAYMENT_PENDING),
  COMPLETED(OrderStatus.CONFIRMED),
  FAILED(OrderStatus.FAILED),
  CANCELED(OrderStatus.CANCELED);

  private final OrderStatus orderStatus;
}
