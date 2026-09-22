package dev.teamuts.payment.app.common.response;

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
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ApiResponse<?> handleException(Exception e) {
    log.error("[COMMON] {}", e.getMessage());

    return ApiResponse.failure(new ApiError(e.getMessage(), ApiErrorCode.INTERNAL_SERVER_ERROR));
  }
}
