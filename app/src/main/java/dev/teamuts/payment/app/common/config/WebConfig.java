package dev.teamuts.payment.app.common.config;

import dev.teamuts.payment.app.pg.PGWebhookArgumentResolver;
import dev.teamuts.shared.error.ProblemDetailGenerator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
  private final PGWebhookArgumentResolver pgWebhookArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(pgWebhookArgumentResolver);
  }

  @Bean
  public ProblemDetailGenerator problemDetailGenerator(
      MessageSource messageSource, @Value("${spring.mvc.problemdetails.base-url}") String baseUrl) {
    return new ProblemDetailGenerator(messageSource, baseUrl);
  }
}
