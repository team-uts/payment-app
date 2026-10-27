package dev.teamuts.payment.app.common.response;

import dev.teamuts.payment.shared.config.AppProfile;
import dev.teamuts.payment.shared.error.ProblemDetailGenerator;
import dev.teamuts.payment.shared.error.constant.CommonErrorCode;
import dev.teamuts.payment.shared.error.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {
  private final AppProfile appProfile;
  private final MessageSource messageSource;
  private final ProblemDetailGenerator problemDetailGenerator;

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception e, HttpServletRequest request, Locale locale) {
    CommonErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;

    if (appProfile.isLocal()) {
      log.error("[{}]", errorCode.name(), e);
    }

    log.error("[COMMON] Exception occurred {}", e.getMessage());

    return problemDetailGenerator.generate(errorCode, request, locale);
  }

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(BusinessException.class)
  public ApiResponse<?> handleBusinessException(BusinessException e, Locale locale) {
    log.error("[COMMON] Business exception occurred {}", e.getMessage());

    ErrorResponse errorResponse =
        ErrorResponse.of(
            e.getErrorCode(),
            messageSource.getMessage(e.getErrorCode().getMessageKey(), e.getArgs(), locale));

    return ApiResponse.failure(errorResponse);
  }
}
