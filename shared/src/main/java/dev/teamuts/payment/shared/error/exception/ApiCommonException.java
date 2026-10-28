package dev.teamuts.payment.shared.error.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ApiCommonException extends RuntimeException {
  private final ErrorCode errorCode;
  private final ErrorArguments errorArgs;

  protected ApiCommonException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode;
    this.errorArgs = ErrorArguments.empty();
  }

  protected ApiCommonException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.name(), cause);
    this.errorCode = errorCode;
    this.errorArgs = ErrorArguments.empty();
  }

  protected ApiCommonException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode.name());
    this.errorCode = errorCode;
    this.errorArgs = errorArgs;
  }

  public Object[] getArgs() {
    return errorArgs.getArgs();
  }
}
