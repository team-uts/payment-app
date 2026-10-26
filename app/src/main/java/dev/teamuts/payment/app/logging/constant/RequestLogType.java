package dev.teamuts.payment.app.logging.constant;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RequestLogType {
  REQUEST_ID("request_id", request -> UUID.randomUUID().toString().substring(0, 8)),
  REQUEST_URI("request_uri", HttpServletRequest::getRequestURI),
  HTTP_METHOD("http_method", HttpServletRequest::getMethod),
  REQUEST_QUERY_PARAM("request_query_param", HttpServletRequest::getQueryString),
  ;

  private final String key;
  private final Function<HttpServletRequest, String> valueExtractor;

  public static void putAllLogsToMdc(HttpServletRequest request) {
    Arrays.stream(RequestLogType.values())
        .forEach(
            logType -> {
              MDC.put(logType.key, logType.valueExtractor.apply(request));
            });
  }
}
