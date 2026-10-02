package dev.teamuts.payment.app.pg;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.usecase.GetPGWebhookHeaderNameUseCase;
import dev.teamuts.payment.domain.pg.usecase.ParseValidPGWebhookEventUseCase;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

@Slf4j
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
  public ExtPGWebhookEventDto resolveArgument(
      MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory)
      throws Exception {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

    PGRequestType requestType = PGRequestType.fromPathName(getPathVariable(request, "requestType"));
    PGProviderType pgProvider = PGProviderType.fromPGName(getPathVariable(request, "pgProvider"));

    // Get the webhook header name based on the PG provider
    String secretHeaderName = getPGWebhookHeaderNameUseCase.execute(pgProvider);

    String payload = getPayloadFromRequest(request);
    String secretFromHeader = request.getHeader(secretHeaderName);

    // Validate the webhook and retrieve important information from the webhook event
    return parseValidPGWebhookEventUseCase.execute(
        requestType, pgProvider, secretFromHeader, payload);
  }

  @SuppressWarnings("unchecked")
  private String getPathVariable(HttpServletRequest request, String name) {
    Map<String, String> pathVariables =
        (Map<String, String>)
            Objects.requireNonNull(request)
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    return pathVariables.get(name);
  }

  private String getPayloadFromRequest(HttpServletRequest request) throws Exception {
    ServletServerHttpRequest inputMessage =
        new ServletServerHttpRequest(Objects.requireNonNull(request));
    return StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
  }
}
