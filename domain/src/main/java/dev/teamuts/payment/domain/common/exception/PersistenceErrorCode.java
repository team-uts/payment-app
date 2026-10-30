package dev.teamuts.payment.domain.common.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PersistenceErrorCode implements ErrorCode {
  NOT_FOUND("PERS-0001"),
  NOT_PERSISTED("PERS-0002");

  private final String code;
  // Persistence layer errors > Internal Server Error (500)
  private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  @Override
  public String getMessageKey() {
    return "persistence." + name();
  }
}
