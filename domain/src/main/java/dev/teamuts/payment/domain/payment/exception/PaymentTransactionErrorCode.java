package dev.teamuts.payment.domain.payment.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentTransactionErrorCode implements ErrorCode {
  CANNOT_GENERATE_WITH_NO_PAY_AMOUNT("PAYTX-0021", HttpStatus.BAD_REQUEST),
  CANNOT_GENERATE_WITH_NO_POINT_AMOUNT("PAYTX-0022", HttpStatus.BAD_REQUEST),
  CANNOT_GENERATE_TXS("PAYTX-0023", HttpStatus.BAD_REQUEST),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public String getMessageKey() {
    return "payment.tx." + name();
  }
}
