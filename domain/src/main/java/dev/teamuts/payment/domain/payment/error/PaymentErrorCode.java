package dev.teamuts.payment.domain.payment.error;

import dev.teamuts.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentErrorCode implements ErrorCode {
  CANNOT_BE_GENERATED("PAY-0021", HttpStatus.BAD_REQUEST),
  ONLY_FOR_POINT_CANNOT_BE_GENERATED("PAY-0022", HttpStatus.BAD_REQUEST),
  NOT_PROCESSING_STATUS("PAY-0023", HttpStatus.BAD_REQUEST),
  HAS_NOT_SAME_CURRENCY("PAY-0024", HttpStatus.BAD_REQUEST),
  CANNOT_UPDATE_OUTBOX_EVENT("PAY-0025", HttpStatus.BAD_REQUEST),
  INVALID_TRANSACTIONS("PAY-0026", HttpStatus.BAD_REQUEST),
  INVALID_OUTBOX_EVENT("PAY-0027", HttpStatus.BAD_REQUEST),

  CANNOT_PROCESS_PAY_TX("PAY-0101", HttpStatus.BAD_REQUEST),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public @NonNull String getMessageKey() {
    return "payment." + name();
  }
}
