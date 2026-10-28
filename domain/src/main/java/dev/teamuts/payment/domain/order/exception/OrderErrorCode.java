package dev.teamuts.payment.domain.order.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderErrorCode implements ErrorCode {
  NOT_FOUND("ORD-0001", HttpStatus.NOT_FOUND),

  MEMBER_MISMATCH("ORD-0011", HttpStatus.FORBIDDEN),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public String getMessageKey() {
    return "order." + name();
  }
}
