package dev.teamuts.payment.app.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  private ApiResultType result;
  private T data;
  private final ErrorResponse error;

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(ApiResultType.SUCCESS, data, null);
  }

  public static <T> ApiResponse<T> failure(ErrorResponse errorResponse) {
    return new ApiResponse<>(ApiResultType.FAILURE, null, errorResponse);
  }
}
