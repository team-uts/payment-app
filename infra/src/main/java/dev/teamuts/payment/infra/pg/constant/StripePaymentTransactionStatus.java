package dev.teamuts.payment.infra.pg.constant;

import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Status of this PaymentIntent, one of {@code requires_payment_method}, {@code
 * requires_confirmation}, {@code requires_action}, {@code processing}, {@code requires_capture},
 * {@code canceled}, or {@code succeeded}. Read more about each PaymentIntent <a
 * href="https://docs.stripe.com/payments/intents#intent-statuses">status</a>.
 */
@Getter
@AllArgsConstructor
public enum StripePaymentTransactionStatus {
  REQUIRES_PAYMENT_METHOD(PaymentTransactionStatus.FAILED),
  REQUIRES_CONFIRMATION(PaymentTransactionStatus.READY),
  REQUIRES_ACTION(PaymentTransactionStatus.ACTION_REQUIRED),
  PROCESSING(PaymentTransactionStatus.PENDING),
  CANCELED(PaymentTransactionStatus.CANCELED),
  SUCCEEDED(PaymentTransactionStatus.COMPLETED);

  private final PaymentTransactionStatus domainStatus;

  private static final Map<String, StripePaymentTransactionStatus> STRIPE_TX_STATUS_MAP =
      Arrays.stream(StripePaymentTransactionStatus.values())
          .collect(Collectors.toUnmodifiableMap(StripePaymentTransactionStatus::name, e -> e));

  public static StripePaymentTransactionStatus of(String status) {
    StripePaymentTransactionStatus statusType =
        STRIPE_TX_STATUS_MAP.getOrDefault(status.toUpperCase(), null);

    if (statusType == null) {
      throw new IllegalArgumentException(
          "Invalid StripePaymentTransactionStatus: %s".formatted(status));
    }

    return statusType;
  }
}
