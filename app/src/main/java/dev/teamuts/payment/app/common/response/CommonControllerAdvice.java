package dev.teamuts.payment.app.common.response;

import dev.teamuts.payment.domain.common.exception.PersistenceException;
import dev.teamuts.payment.shared.config.AppProfile;
import dev.teamuts.payment.shared.error.ProblemDetailGenerator;
import dev.teamuts.payment.shared.error.constant.CommonErrorCode;
import dev.teamuts.payment.shared.error.exception.ApiCommonException;
import dev.teamuts.payment.shared.error.exception.BusinessException;
import dev.teamuts.payment.shared.error.exception.SystemException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

  /**
   * Handle System Exception (Unexpected Exception). Default response status = 500 Internal Server
   * Error. Always return with problem detail.
   *
   * @param e {@link Exception} {@link SystemException} {@link PersistenceException}
   */
  @ApiResponse(
      responseCode = "500",
      description = "Internal Server Error",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
              schema = @Schema(implementation = ProblemDetail.class),
              examples =
                  @ExampleObject(
                      value =
                          """
                          {
                            "type": "https://api.payment.teamuts.dev/problem/internal-server-error",
                            "title": "INTERNAL_SERVER_ERROR",
                            "status": 500,
                            "code": "COM-0001",
                            "detail": "An unexpected error occurred. Please try again later.",
                            "instance": "/v1/orders/12345"
                          }""")))
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception e, HttpServletRequest request, Locale locale) {
    SystemException systemException =
        SystemException.fromOrDefault(e, CommonErrorCode.INTERNAL_SERVER_ERROR);

    logException(systemException);

    return problemDetailGenerator.generate(systemException, request, locale);
  }

  /** Handle Business Logic Exception. Always return HTTP 200 OK with error code. */
  @ApiResponse(
      responseCode = "200",
      description = "Business Exception",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ApiCommonResponse.class),
              examples =
                  @ExampleObject(
                      value =
                          """
                          {
                            "result": "FAILURE",
                            "error": {
                              "code": "ORD-0011",
                              "type": "ORDER_MEMBER_MISMATCH",
                              "message": "Order (extOrderNum: {0}) does not belong to member (memberId: {1})."
                            }
                          }""")))
  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(BusinessException.class)
  public ApiCommonResponse<?> handleBusinessException(BusinessException e, Locale locale) {
    logException(e);

    ErrorResponse errorResponse =
        ErrorResponse.of(
            e.getErrorCode(),
            messageSource.getMessage(e.getErrorCode().getMessageKey(), e.getArgs(), locale));

    return ApiCommonResponse.failure(errorResponse);
  }

  private void logException(ApiCommonException e) {
    String logMessage = e.getMessage();

    if (Objects.requireNonNull(e) instanceof SystemException systemException) {
      logMessage = systemException.getLogMessage();
    }

    if (appProfile.isLocal()) {
      log.error(
          "Code: {} / Exception: {} / Msg: {} /",
          e.getErrorCode().name(),
          e.getClass().getSimpleName(),
          logMessage,
          e.getCause());
      return;
    }

    log.error(
        "Code: {} / Exception: {} / Msg: {}",
        e.getErrorCode().name(),
        e.getClass().getSimpleName(),
        logMessage);
  }
}
