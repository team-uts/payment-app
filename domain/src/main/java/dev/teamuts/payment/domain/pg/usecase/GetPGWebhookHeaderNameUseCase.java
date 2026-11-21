package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.service.ExtPGServiceProvider;
import dev.teamuts.shared.domain.annotation.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPGWebhookHeaderNameUseCase {
  private final ExtPGServiceProvider pgServiceProvider;

  public String execute(PgProviderType pgProvider) {
    return pgServiceProvider.getInstance(pgProvider).getWebhookHeaderName();
  }
}
