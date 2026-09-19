package dev.teamuts.payment.infra.pg.dto;

import dev.teamuts.payment.infra.pg.constant.ExtPGOperationType;
import dev.teamuts.payment.infra.pg.constant.ExtPGResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class BaseExtPGResponse<T> {
  private T data;
  private ExtPGResponseStatus status;
  private ExtPGOperationType operationType;
  private String errorMessage;

  public static <T> BaseExtPGResponse<T> succeeded(T data, ExtPGOperationType operationType) {
    return new BaseExtPGResponse<>(data, ExtPGResponseStatus.SUCCESS, operationType, null);
  }

  public static <T> BaseExtPGResponse<T> failed(
      ExtPGOperationType operationType, String errorMessage) {
    return new BaseExtPGResponse<>(
        null,
        ExtPGResponseStatus.FAILED,
        operationType,
        "Failed [%s]: %s".formatted(operationType.name(), errorMessage));
  }

  public <X extends Throwable> T orElseThrow() {
    if (status == ExtPGResponseStatus.FAILED) {
      throw new RuntimeException("[%s] %s".formatted(operationType, errorMessage));
    }

    if (data == null) {
      throw new RuntimeException("[%s] ExtPGBaseResponse data is null".formatted(operationType));
    }

    return data;
  }
}
