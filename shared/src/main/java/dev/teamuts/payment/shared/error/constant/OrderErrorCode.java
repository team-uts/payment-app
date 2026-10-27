package dev.teamuts.payment.shared.error.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderErrorCode implements ErrorCode {
  ORDER_NOT_FOUND("ORD-0001", HttpStatus.NOT_FOUND),
  ORDER_ALREADY_PAID("ORD-0002", HttpStatus.CONFLICT),
  ORDER_ALREADY_CANCELLED("ORD-0003", HttpStatus.CONFLICT),
  ORDER_PAYMENT_FAILED("ORD-0004", HttpStatus.BAD_REQUEST),

  ORDER_MEMBER_MISMATCH("ORD-0011", HttpStatus.FORBIDDEN),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public String getMessageKey() {
    return "order." + name();
  }
}
