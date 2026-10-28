package dev.teamuts.payment.persistence.common.exception;

import dev.teamuts.payment.domain.common.model.BaseIdentifiableModel;
import dev.teamuts.payment.shared.error.constant.ErrorCode;
import dev.teamuts.payment.shared.error.exception.ErrorArguments;
import dev.teamuts.payment.shared.error.exception.SystemException;

public class PersistenceException extends SystemException {
  private PersistenceException(ErrorCode errorCode, String logMessage, String detailedMessage) {
    super(errorCode, logMessage, detailedMessage);
  }

  private PersistenceException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause, null, null);
  }

  private PersistenceException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode, errorArgs, null, null);
  }

  public static PersistenceException notFound(BaseIdentifiableModel<Long> model) {
    return new PersistenceException(
        PersistenceErrorCode.NOT_FOUND,
        "Entity (%s) not found with id (%d) in the persistence layer"
            .formatted(model.getClass().getSimpleName(), model.getId()),
        null);
  }

  public static PersistenceException notFound(
      Class<? extends BaseIdentifiableModel<Long>> modelClazz, Long id) {
    return new PersistenceException(
        PersistenceErrorCode.NOT_FOUND,
        "Entity (%s) not found with id (%d) in the persistence layer"
            .formatted(modelClazz.getSimpleName(), id),
        null);
  }
}
