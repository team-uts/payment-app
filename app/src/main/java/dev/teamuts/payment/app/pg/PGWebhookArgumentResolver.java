package dev.teamuts.payment.app.pg;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.PGExtOperationInfo.WebhookEventInfo;
import dev.teamuts.payment.domain.pg.usecase.GetPGWebhookHeaderNameUseCase;
import dev.teamuts.payment.domain.pg.usecase.ParseValidPGWebhookEventUseCase;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class PGWebhookArgumentResolver implements HandlerMethodArgumentResolver {
  private final GetPGWebhookHeaderNameUseCase getPGWebhookHeaderNameUseCase;
  private final ParseValidPGWebhookEventUseCase parseValidPGWebhookEventUseCase;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(PGWebhookPayload.class);
  }

  @Override
  public WebhookEventInfo resolveArgument(
      MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory)
      throws Exception {

    PGWebhookPayload annotation = getValidPgWebhookPayload(parameter);
    PGRequestType requestType = annotation.requestType();
    PGProviderType pgProvider = annotation.pgProvider();

    // Get the webhook header name based on the PG provider
    String secretHeaderName = getPGWebhookHeaderNameUseCase.execute(pgProvider);

    String payload = getPayloadFromRequest(webRequest);
    String secretFromHeader = webRequest.getHeader(secretHeaderName);

    // Validate the webhook and retrieve important information from the webhook event
    return parseValidPGWebhookEventUseCase.execute(
        requestType, pgProvider, secretFromHeader, payload);
  }

  private static PGWebhookPayload getValidPgWebhookPayload(MethodParameter parameter) {
    PGWebhookPayload annotation = parameter.getParameterAnnotation(PGWebhookPayload.class);

    if (annotation == null || annotation.requestType() == null || annotation.pgProvider() == null) {
      throw new IllegalArgumentException(
          "PGWebhookPayload annotation is missing or invalid on parameter: "
              + parameter.getParameterName());
    }

    return annotation;
  }

  private String getPayloadFromRequest(NativeWebRequest webRequest) throws Exception {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    ServletServerHttpRequest inputMessage =
        new ServletServerHttpRequest(Objects.requireNonNull(request));
    return StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
  }
}
