package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.service.ExtPGServiceProvider;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ParseValidPGWebhookEventUseCase {
  private final ExtPGServiceProvider pgServiceProvider;

  public ExtPGWebhookEventDto execute(
      PGRequestType requestType, PGProviderType pgProvider, String secret, String payload) {

    return pgServiceProvider
        .getInstance(pgProvider)
        .parseWebhookEvent(requestType, payload, secret);
  }
}
