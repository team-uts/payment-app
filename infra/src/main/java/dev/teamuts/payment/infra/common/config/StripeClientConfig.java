package dev.teamuts.payment.infra.common.config;

import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class StripeClientConfig {
  @Value("${stripe.secret-key}")
  private String stripeSecretKey;

  @Bean
  public StripeClient stripeClient() {
    return new StripeClient(stripeSecretKey);
  }
}
