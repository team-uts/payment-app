package dev.teamuts.payment.infra.pg.dto;

import dev.teamuts.payment.infra.pg.constant.ExtPGApiType;
import dev.teamuts.payment.infra.pg.constant.ExtPGResponseStatus;

public record ExtPGBaseResponse<T>(
    T data, ExtPGResponseStatus status, ExtPGApiType apiType, String errorMessage) {
  public static <T> ExtPGBaseResponse<T> succeeded(T data, ExtPGApiType apiType) {
    return new ExtPGBaseResponse<>(data, ExtPGResponseStatus.SUCCESS, apiType, null);
  }

  public static <T> ExtPGBaseResponse<T> failed(ExtPGApiType apiType, String errorMessage) {
    return new ExtPGBaseResponse<>(null, ExtPGResponseStatus.FAILED, apiType, errorMessage);
  }

  public <X extends Throwable> T orElseThrow() {
    if (status == ExtPGResponseStatus.FAILED) {
      throw new RuntimeException("[%s] %s".formatted(apiType, errorMessage));
    }

    if (data == null) {
      throw new RuntimeException("[%s] ExtPGBaseResponse data is null".formatted(apiType));
    }

    return data;
  }
}
