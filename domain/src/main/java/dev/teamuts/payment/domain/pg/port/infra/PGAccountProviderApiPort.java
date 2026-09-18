package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;

public interface PGAccountProviderApiPort {
  String createNewAccount(Long memberId, String email, PGProviderType pgProvider);
}
