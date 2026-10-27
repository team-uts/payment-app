package dev.teamuts.payment.shared.error.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PgExternalRequestErrorCode implements ErrorCode {
  NOT_FOUND_WITH_PARAMS("PER-0002", HttpStatus.NOT_FOUND),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public String getMessageKey() {
    return "";
  }
}
