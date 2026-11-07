package dev.teamuts.payment.domain.payment.exception;

import dev.teamuts.shared.error.constant.ErrorCode;
import dev.teamuts.shared.error.exception.BusinessException;
import dev.teamuts.shared.error.exception.ErrorArguments;

public class PaymentException extends BusinessException {
  private PaymentException(ErrorCode errorCode) {
    super(errorCode);
  }

  private PaymentException(ErrorCode errorCode, Object... args) {
    super(errorCode, ErrorArguments.of(args));
  }

  public static PaymentException of(PaymentErrorCode errorCode, Object... args) {
    return new PaymentException(errorCode, args);
  }

  public static PaymentException of(PaymentTransactionErrorCode errorCode, Object... args) {
    return new PaymentException(errorCode, args);
  }
}
