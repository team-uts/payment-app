package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPGWebhookSecretKeyUseCase {
  public String execute(PGRequestType requestType, PGProviderType pgProvider) {
    return "";
  }
}
