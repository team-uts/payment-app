package dev.teamuts.payment.domain.payment.constant;

import java.util.Set;

/** PENDING: specially, handling PG Timeout issue */
public enum PaymentTransactionStatus {
  INIT,
  PENDING,
  COMPLETED,
  FAILED;

  public static final Set<PaymentTransactionStatus> PROCESSABLE_STATUSES = Set.of(INIT, PENDING);
}
