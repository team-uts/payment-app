package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.service.ExtPGServiceProvider;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPGWebhookHeaderNameUseCase {
  private final ExtPGServiceProvider pgServiceProvider;

  public String execute(PGProviderType pgProvider) {
    return pgServiceProvider.getInstance(pgProvider).getWebhookHeaderName();
  }
}
