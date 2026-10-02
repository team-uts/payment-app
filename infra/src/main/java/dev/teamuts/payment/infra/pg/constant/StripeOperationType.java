package dev.teamuts.payment.infra.pg.constant;

import static dev.teamuts.payment.domain.pg.constant.PGRequestType.*;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StripeOperationType {
  CREATE_CUSTOMER(ACCOUNT, "user_id", false),
  CREATE_SETUP_INTENT(PAYMENT_METHOD_SETUP, "user_id", false),
  CREATE_PAYMENT_INTENT(PAYMENT, null, false),
  WEBHOOK_SETUP_INTENT(PAYMENT_METHOD_SETUP, "user_id", true),
  WEBHOOK_PAYMENT_INTENT(PAYMENT, null, true);

  private final PGRequestType requestType;
  private final String metadataKey;
  private final Boolean webhookOperation;
}
