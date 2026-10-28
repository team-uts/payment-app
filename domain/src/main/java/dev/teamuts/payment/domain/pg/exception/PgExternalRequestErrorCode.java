package dev.teamuts.payment.domain.pg.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PgExternalRequestErrorCode implements ErrorCode {
  NOT_FOUND("PER-0001", HttpStatus.NOT_FOUND),
  NOT_FOUND_WITH_PARAMS("PER-0002", HttpStatus.NOT_FOUND),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public String getMessageKey() {
    return "pg_external_request." + name();
  }
}
