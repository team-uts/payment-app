package dev.teamuts.payment.domain.payment.constant;

import dev.teamuts.payment.domain.order.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum PaymentStatus {
  PROCESSING(OrderStatus.PAYMENT_PENDING, false),
  PENDING(OrderStatus.PAYMENT_PENDING, false),
  COMPLETED(OrderStatus.CONFIRMED, true),
  FAILED(OrderStatus.FAILED, true),
  CANCELED(OrderStatus.CANCELED, true);

  private final OrderStatus orderStatus;
  private final boolean isFinal;
}
