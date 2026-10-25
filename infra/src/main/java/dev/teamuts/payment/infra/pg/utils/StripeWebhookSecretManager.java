package dev.teamuts.payment.infra.pg.utils;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.infra.common.config.StripeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StripeWebhookSecretManager {
  private final StripeProperties stripeProperties;

  public String getEndpointSecret(PGRequestType requestType) {
    return switch (requestType) {
      case PAYMENT_METHOD_SETUP -> stripeProperties.webhook().secret().setupIntent();
      case PAYMENT -> stripeProperties.webhook().secret().paymentIntent();
      default -> throw new IllegalArgumentException("Invalid request type: " + requestType);
    };
  }
}
