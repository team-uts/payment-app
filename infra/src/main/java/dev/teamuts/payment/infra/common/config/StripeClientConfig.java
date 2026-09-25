package dev.teamuts.payment.infra.common.config;

import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(StripeWebhookProperties.class)
public class StripeClientConfig {
  @Value("${stripe.secret-key}")
  private String stripeSecretKey;

  @Bean
  public StripeClient stripeClient() {
    return new StripeClient(stripeSecretKey);
  }
}
