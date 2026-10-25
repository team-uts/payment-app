package dev.teamuts.payment.infra.common.config;

import com.stripe.StripeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(StripeProperties.class)
@RequiredArgsConstructor
public class StripeClientConfig {
  private final StripeProperties stripeProperties;

  @Bean
  public StripeClient stripeClient() {
    return StripeClient.builder()
        .setApiKey(stripeProperties.secretKey())
        .setConnectTimeout(stripeProperties.getConnectTimeoutInMillis())
        .setReadTimeout(stripeProperties.getReadTimeoutInMillis())
        .build();
  }
}
