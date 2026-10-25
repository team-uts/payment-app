package dev.teamuts.payment.infra.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public record StripeProperties(
    String secretKey, Webhook webhook, int readTimeoutInSec, int connectTimeoutInSec) {

  public record Webhook(Secret secret) {
    public record Secret(String setupIntent, String paymentIntent) {}
  }

  public static final int MILLI_SEC = 1_000;

  public int getConnectTimeoutInMillis() {
    return connectTimeoutInSec * MILLI_SEC;
  }

  public int getReadTimeoutInMillis() {
    return readTimeoutInSec * MILLI_SEC;
  }
}
