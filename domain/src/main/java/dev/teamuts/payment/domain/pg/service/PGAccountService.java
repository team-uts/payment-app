package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.infra.PGAccountProviderApiPort;
import dev.teamuts.payment.domain.pg.port.persistence.PGAccountReaderPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PGAccountService {
  private final PGAccountReaderPort pgAccountReaderPort;
  private final PGAccountProviderApiPort pgAccountProviderApiPort;

  public Optional<PGAccount> findPGAccountByMemberId(Long memberId) {
    return pgAccountReaderPort.retrievePGAccountOptionalByMemberId(memberId);
  }

  public PGAccount createPGAccount(Long memberId, String email, PGProviderType providerType) {
    String pgAccountId = pgAccountProviderApiPort.createNewAccount(memberId, email, providerType);

    return PGAccount.activateNew(memberId, pgAccountId, providerType);
  }
}
