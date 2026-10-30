package dev.teamuts.payment.domain.common.exception;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum InfraErrorCode implements ErrorCode {
  PG_OPERATION_ERROR("INFRA-0011"),
  PG_WEBHOOK_DESERIALIZATION_FAILED("INFRA-0101"),
  ;

  private final String code;
  // Infra layer errors > Internal Server Error (500)
  private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  @Override
  public String getMessageKey() {
    return "infra." + name();
  }
}
