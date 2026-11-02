package dev.teamuts.payment.app.common.response;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
  private final String code;
  private final String type;
  private final String message;

  public static ErrorResponse of(ErrorCode errorCode, String message) {
    return new ErrorResponse(errorCode.getCode(), errorCode.name(), message);
  }
}
