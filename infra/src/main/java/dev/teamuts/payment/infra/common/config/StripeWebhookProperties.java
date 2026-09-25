package dev.teamuts.payment.infra.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe.webhook")
@Getter
@AllArgsConstructor
public class StripeWebhookProperties {
  private Secret secret;

  @Getter
  @AllArgsConstructor
  private static class Secret {
    private String setupIntent;
  }
}
