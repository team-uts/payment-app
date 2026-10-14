package dev.teamuts.payment.domain.payment.constant;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** PENDING: specially, handling PG Timeout issue */
@Getter
@AllArgsConstructor
public enum PaymentTransactionStatus {
  INIT(false),
  CREATED(false),
  PENDING(false), // should check first the PG transaction status from PG provider.
  ACTION_REQUIRED(false), // e.g., 3D authentication required (on banking app)
  COMPLETED(true),
  CANCELED(true),
  FAILED(true);

  private final boolean isFinal;

  public static final Set<PaymentTransactionStatus> PG_PAY_PROCESSABLE_STATUSES =
      Set.of(INIT, CREATED);
}
