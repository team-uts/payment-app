package dev.teamuts.payment.infra.pg.utils;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.infra.common.config.StripeWebhookProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StripeWebhookSecretManager {
  private final StripeWebhookProperties webhookProperties;

  public String getEndpointSecret(PGRequestType requestType) {
    return switch (requestType) {
      case PAYMENT_METHOD_SETUP -> webhookProperties.getSecret().getSetupIntent();
      case PAYMENT -> webhookProperties.getSecret().getPaymentIntent();
      default -> throw new IllegalArgumentException("Invalid request type: " + requestType);
    };
  }
}
