package dev.teamuts.payment.shared.error.exception;

import dev.teamuts.payment.shared.error.constant.OrderErrorCode;

public class OrderException extends BusinessException {
  private OrderException(OrderErrorCode errorCode) {
    super(errorCode);
  }

  private OrderException(OrderErrorCode errorCode, ErrorArguments args) {
    super(errorCode, args);
  }

  public static OrderException of(OrderErrorCode errorCode, ErrorArguments args) {
    return new OrderException(errorCode, args);
  }
}
