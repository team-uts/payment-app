package dev.teamuts.payment.shared.error.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;

public class BusinessException extends ApiCommonException {
  protected BusinessException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected BusinessException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode, errorArgs);
  }

  public static BusinessException of(ErrorCode errorCode, ErrorArguments errorArgs) {
    return new BusinessException(errorCode, errorArgs);
  }
}
