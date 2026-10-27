package dev.teamuts.payment.shared.error.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final ErrorCode errorCode;
  private final ErrorArguments errorArgs;

  protected BusinessException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode;
    this.errorArgs = ErrorArguments.empty();
  }

  protected BusinessException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode.name());
    this.errorCode = errorCode;
    this.errorArgs = errorArgs;
  }

  public static BusinessException of(ErrorCode errorCode, ErrorArguments errorArgs) {
    return new BusinessException(errorCode, errorArgs);
  }

  public Object[] getArgs() {
    return errorArgs.getArgs();
  }
}
