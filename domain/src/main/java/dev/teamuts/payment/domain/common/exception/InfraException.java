package dev.teamuts.payment.domain.common.exception;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.shared.error.constant.ErrorCode;
import dev.teamuts.payment.shared.error.exception.ErrorArguments;
import dev.teamuts.payment.shared.error.exception.SystemException;

public class InfraException extends SystemException {
  private InfraException(ErrorCode errorCode, String logMessage, String detailedMessage) {
    super(errorCode, logMessage, detailedMessage);
  }

  private InfraException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause, null, null);
  }

  private InfraException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode, errorArgs, null, null);
  }

  public static InfraException of(InfraErrorCode errorCode, String logMessage) {
    return new InfraException(errorCode, logMessage, null);
  }

  public static InfraException pgOperationError(
      InfraErrorCode errorCode, PGProviderType pgProvider, String operationType) {
    return new InfraException(
        errorCode, "[%s] %s - failed".formatted(pgProvider, operationType), null);
  }
}
