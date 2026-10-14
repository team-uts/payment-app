package dev.teamuts.payment.app.common.response;

import dev.teamuts.payment.shared.config.AppProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {
  private final AppProfile appProfile;

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ApiResponse<?> handleException(Exception e) {
    if (appProfile.isLocal()) {
      log.error("[COMMON]", e);
    }

    log.error("[COMMON] Exception occurred {}", e.getMessage());

    return ApiResponse.failure(
        new ApiError("Internal server error", ApiErrorCode.INTERNAL_SERVER_ERROR));
  }
}
