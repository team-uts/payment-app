package dev.teamuts.payment.domain.pg.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PGWebhookEventProcessResultType {
  SUCCESS(PGRequestStatus.COMPLETED),
  FAILURE(PGRequestStatus.FAILED),
  PENDING(null);

  private final PGRequestStatus requestStatus;

  public PGRequestStatus getPGRequestStatus() {
    if (requestStatus == null) {
      throw new UnsupportedOperationException("no PGRequestStatus mapping for " + name());
    }
    return requestStatus;
  }
}
