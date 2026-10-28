package dev.teamuts.payment.domain.payment.exception;

import dev.teamuts.payment.shared.error.exception.BusinessException;
import dev.teamuts.payment.shared.error.exception.ErrorArguments;

public class PaymentException extends BusinessException {
  private PaymentException(PaymentErrorCode errorCode) {
    super(errorCode);
  }

  private PaymentException(PaymentErrorCode errorCode, Object... args) {
    super(errorCode, ErrorArguments.of(args));
  }

  public static PaymentException of(PaymentErrorCode errorCode, Object... args) {
    return new PaymentException(errorCode, ErrorArguments.of(args));
  }
}
