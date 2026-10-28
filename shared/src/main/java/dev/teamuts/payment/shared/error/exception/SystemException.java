package dev.teamuts.payment.shared.error.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.Getter;

@Getter
public class SystemException extends ApiCommonException {
  private final String logMessage;
  private final String detailedMessage;

  protected SystemException(ErrorCode errorCode, String logMessage, String detailedMessage) {
    super(errorCode);
    this.logMessage = logMessage;
    this.detailedMessage = detailedMessage;
  }

  protected SystemException(
      ErrorCode errorCode, Throwable cause, String logMessage, String detailedMessage) {
    super(errorCode, cause);
    this.logMessage = logMessage;
    this.detailedMessage = detailedMessage;
  }

  protected SystemException(
      ErrorCode errorCode, ErrorArguments errorArgs, String logMessage, String detailedMessage) {
    super(errorCode, errorArgs);
    this.logMessage = logMessage;
    this.detailedMessage = detailedMessage;
  }

  public static SystemException of(ErrorCode errorCode) {
    return new SystemException(errorCode, null, null);
  }

  public static SystemException fromOrDefault(Throwable cause, ErrorCode defaultCode) {
    if (cause instanceof SystemException systemException) {
      return systemException;
    }

    return new SystemException(defaultCode, cause, null, null);
  }

  public static SystemException of(ErrorCode errorCode, ErrorArguments errorArgs) {
    return new SystemException(errorCode, errorArgs, null, null);
  }
}
