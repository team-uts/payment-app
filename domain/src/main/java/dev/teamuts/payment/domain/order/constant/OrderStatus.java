package dev.teamuts.payment.domain.order.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum OrderStatus {
  INIT(false),
  PAYMENT_PENDING(false),
  CONFIRMED(true),
  CANCELED(true),
  FAILED(true);

  private final boolean isFinal;
}
