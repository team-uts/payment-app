package dev.teamuts.payment.app.pg;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;

public class PGWebhookResult {
  private PGRequestType requestType;
  private PGProviderType pgProvider;
  private String pgRequestId;
  private String providerToken;
}
