package dev.teamuts.payment.domain.order.exception;

import dev.teamuts.payment.shared.error.exception.BusinessException;
import dev.teamuts.payment.shared.error.exception.ErrorArguments;

public class OrderException extends BusinessException {
  private OrderException(OrderErrorCode errorCode) {
    super(errorCode);
  }

  private OrderException(OrderErrorCode errorCode, Object... args) {
    super(errorCode, ErrorArguments.of(args));
  }

  public static OrderException of(OrderErrorCode errorCode, Object... args) {
    return new OrderException(errorCode, args);
  }
}
