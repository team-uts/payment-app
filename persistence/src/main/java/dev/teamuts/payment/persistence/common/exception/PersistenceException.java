package dev.teamuts.payment.persistence.common.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import dev.teamuts.payment.shared.error.exception.ApiCommonException;
import dev.teamuts.payment.shared.error.exception.ErrorArguments;

public class PersistenceException extends ApiCommonException {
  private PersistenceException(ErrorCode errorCode) {
    super(errorCode);
  }

  private PersistenceException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }

  private PersistenceException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode, errorArgs);
  }

  public static PersistenceException of(ErrorCode errorCode) {
    return new PersistenceException(errorCode);
  }
}
