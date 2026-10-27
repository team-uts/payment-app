package dev.teamuts.payment.app.logging;

import dev.teamuts.payment.app.logging.constant.RequestLogType;
import dev.teamuts.payment.app.security.AppRequestMatcherHolder;
import dev.teamuts.payment.shared.config.AppProfile;
import dev.teamuts.payment.shared.utils.TimeUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@NullMarked
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReqResLoggingFilter extends OncePerRequestFilter {
  private final AppRequestMatcherHolder appRequestMatcherHolder;
  private final AppProfile appProfile;

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    return appRequestMatcherHolder
        .getShouldNotLoggingFilterMatchers()
        .toOrRequestMatcher()
        .matches(request);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, -1);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    RequestLogType.putAllLogsToMdc(requestWrapper);

    long startTime = System.currentTimeMillis();
    filterChain.doFilter(requestWrapper, responseWrapper);
    long endTime = System.currentTimeMillis();

    double elapsedTime = TimeUtils.convertMillisToSecWithPrecision(endTime - startTime);

    try {
      log.info(
          HttpLogMessage.newInstance(requestWrapper, responseWrapper, elapsedTime)
              .getLogMsg(appProfile));

      responseWrapper.copyBodyToResponse();
    } catch (Exception e) {
      log.error("[{}] Logging failed", this.getClass().getSimpleName(), e);
    }
  }
}
