package dev.teamuts.payment.shared.error.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.Getter;

@Getter
public class SystemException extends ApiCommonException {

  private SystemException(ErrorCode errorCode) {
    super(errorCode);
  }

  private SystemException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }

  private SystemException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode, errorArgs);
  }

  public static SystemException of(ErrorCode errorCode) {
    return new SystemException(errorCode);
  }

  public static SystemException fromOrDefault(Throwable cause, ErrorCode defaultCode) {
    if (cause instanceof SystemException systemException) {
      return systemException;
    }

    return new SystemException(defaultCode, cause);
  }

  public static SystemException of(ErrorCode errorCode, ErrorArguments errorArgs) {
    return new SystemException(errorCode, errorArgs);
  }
}
