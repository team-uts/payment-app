package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;

@UseCase
public class ParsePGWebhookResultUseCase {
  public void execute(
      PGRequestType requestType, PGProviderType pgProvider, String secret, String payload) {}
}
