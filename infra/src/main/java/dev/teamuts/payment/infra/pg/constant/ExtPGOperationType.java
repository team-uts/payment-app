package dev.teamuts.payment.infra.pg.constant;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import lombok.Getter;

@Getter
public enum ExtPGOperationType {
  CREATE_CUSTOMER(PGProviderType.STRIPE, PGRequestType.ACCOUNT, "user_id"),
  CREATE_SETUP_INTENT(PGProviderType.STRIPE, PGRequestType.PAYMENT_METHOD_SETUP, "user_id"),
  CREATE_PAYMENT_INTENT(PGProviderType.STRIPE, PGRequestType.PAYMENT, null),
  WEBHOOK_SETUP_INTENT(PGProviderType.STRIPE, PGRequestType.PAYMENT_METHOD_SETUP, "user_id"),
  WEBHOOK_PAYMENT_INTENT(PGProviderType.STRIPE, PGRequestType.PAYMENT, null);

  private final PGProviderType pgProvider;
  private final PGRequestType requestType;
  private final String metadataKey;

  ExtPGOperationType(PGProviderType pgProvider, PGRequestType requestType, String metadataKey) {
    this.pgProvider = pgProvider;
    this.requestType = requestType;
    this.metadataKey = metadataKey;
  }
}
