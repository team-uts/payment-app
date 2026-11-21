package dev.teamuts.payment.domain.event.error;

import dev.teamuts.shared.error.constant.SystemErrorCode;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public enum OutboxEventErrorCode implements SystemErrorCode {
  PAYLOAD_PARSING_ERROR("OET-001"),
  CANNOT_PROCESS("OET-002"),
  ;

  private final String code;
  private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  OutboxEventErrorCode(String code) {
    this.code = code;
  }

  @Override
  public @NonNull String getMessageKey() {
    return "outbox." + name();
  }
}
