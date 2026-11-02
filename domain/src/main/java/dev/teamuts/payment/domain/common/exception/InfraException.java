package dev.teamuts.payment.domain.common.exception;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.shared.error.constant.SystemErrorCode;
import dev.teamuts.payment.shared.error.exception.SystemException;

public class InfraException extends SystemException {
  private InfraException(SystemErrorCode errorCode, String logMessage, String detailedMessage) {
    super(errorCode, logMessage, detailedMessage);
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
