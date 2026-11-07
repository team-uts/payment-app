package dev.teamuts.payment.infra.common.config;

import dev.teamuts.shared.utils.TimeUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public record StripeProperties(
    String secretKey, Webhook webhook, int readTimeoutInSec, int connectTimeoutInSec) {

  public record Webhook(Secret secret) {
    public record Secret(String setupIntent, String paymentIntent) {}
  }

  public int getConnectTimeoutInMillis() {
    return (int) TimeUtils.convertSecToMillis(connectTimeoutInSec);
  }

  public int getReadTimeoutInMillis() {
    return (int) TimeUtils.convertSecToMillis(readTimeoutInSec);
  }
}
