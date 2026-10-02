package dev.teamuts.payment.app.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
  private ApiResultType result;
  private T data;
  private ApiError error;

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(ApiResultType.SUCCESS, data, null);
  }

  public static <T> ApiResponse<T> failure(ApiError error) {
    return new ApiResponse<>(ApiResultType.FAILURE, null, error);
  }
}
