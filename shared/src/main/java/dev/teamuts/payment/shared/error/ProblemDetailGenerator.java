package dev.teamuts.payment.shared.error;

import dev.teamuts.payment.shared.error.constant.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

/**
 * @see <a href="https://www.rfc-editor.org/info/rfc9457">RFC 9457</a>
 */
@Component
@RequiredArgsConstructor
public class ProblemDetailGenerator {
  private final MessageSource messageSource;

  private static final String PROBLEM_BASE_URL = "https://api.payment.teamuts.dev/problem/";

  public ProblemDetail generate(ErrorCode errorCode, HttpServletRequest request, Locale locale) {
    ProblemDetail problem = ProblemDetail.forStatus(errorCode.getHttpStatus());

    problem.setType(getProblemType(errorCode));
    problem.setTitle(errorCode.name());
    problem.setProperty("code", errorCode.getCode());
    problem.setDetail(messageSource.getMessage(errorCode.getMessageKey(), null, locale));
    problem.setInstance(URI.create(request.getRequestURI()));

    return problem;
  }

  private URI getProblemType(ErrorCode errorCode) {
    return URI.create(
        ProblemDetailGenerator.PROBLEM_BASE_URL + errorCode.name().toLowerCase().replace("_", "-"));
  }
}
