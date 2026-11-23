package dev.teamuts.payment.domain.order.error;

import dev.teamuts.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderErrorCode implements ErrorCode {
  NOT_FOUND("ORD-0001", HttpStatus.NOT_FOUND),

  MEMBER_MISMATCH("ORD-0011", HttpStatus.FORBIDDEN),
  HAVE_NO_REMAINING_AMOUNT("ORD-0012", HttpStatus.BAD_REQUEST),
  STATUS_MUST_BE_INIT("ORD-0013", HttpStatus.BAD_REQUEST),
  CANNOT_PAY_PROCESS("ORD-0021", HttpStatus.BAD_REQUEST),
  PAYMENTS_STATUS_NOT_MATCH_ORDER_STATUS("ORD-0022", HttpStatus.BAD_REQUEST);

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public @NonNull String getMessageKey() {
    return "order." + name();
  }
}
