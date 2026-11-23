package dev.teamuts.payment.domain.payment.constant;

import dev.teamuts.payment.domain.order.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum PaymentStatus {
  PROCESSING(OrderStatus.PAYMENT_PENDING, 2, false),
  PENDING(OrderStatus.PAYMENT_PENDING, 3, false),
  COMPLETED(OrderStatus.CONFIRMED, 1, true),
  FAILED(OrderStatus.FAILED, 4, true),
  CANCELED(OrderStatus.CANCELED, 5, true);

  private final OrderStatus orderStatus;
  private int decideOrder;
  private final boolean isFinal;
}
