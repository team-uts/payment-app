package dev.teamuts.payment.app.common.response;

import lombok.Getter;

@Getter
public class ApiError {
  private String message;
  private ApiErrorCode code;

  public ApiError(String message, ApiErrorCode code) {
    this.message = message;
    this.code = code;
  }
}
