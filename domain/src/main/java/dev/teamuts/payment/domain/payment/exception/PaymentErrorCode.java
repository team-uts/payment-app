package dev.teamuts.payment.domain.payment.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentErrorCode implements ErrorCode {
  CANNOT_BE_GENERATED("PAY-0021", HttpStatus.BAD_REQUEST),
  ONLY_FOR_POINT_CANNOT_BE_GENERATED("PAY-0022", HttpStatus.BAD_REQUEST),
  NOT_PROCESSING_STATUS("PAY-0023", HttpStatus.BAD_REQUEST),
  HAS_NOT_SAME_CURRENCY("PAY-0024", HttpStatus.BAD_REQUEST),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public String getMessageKey() {
    return "payment." + name();
  }
}
