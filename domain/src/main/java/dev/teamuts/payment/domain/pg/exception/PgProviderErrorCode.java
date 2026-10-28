package dev.teamuts.payment.domain.pg.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PgProviderErrorCode implements ErrorCode {
  WEBHOOK_VERIFICATION_FAILED("PGP-0001", HttpStatus.BAD_REQUEST),
  WEBHOOK_PAYLOAD_PARSING_FAILED("PGP-0002", HttpStatus.BAD_REQUEST),
  ;
  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public String getMessageKey() {
    return "pg.provider." + name();
  }
}
