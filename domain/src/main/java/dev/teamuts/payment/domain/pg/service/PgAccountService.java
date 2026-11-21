package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.model.PgAccount;
import dev.teamuts.payment.domain.pg.port.persistence.PGAccountReaderPort;
import dev.teamuts.payment.domain.pg.port.persistence.PGAccountStorePort;
import dev.teamuts.shared.data.annotation.AppTransactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PgAccountService {
  private final PGAccountReaderPort pgAccountReaderPort;
  private final PGAccountStorePort pgAccountStorePort;
  private final ExtPGServiceProvider pgServiceProvider;

  public Optional<PgAccount> getPGAccountByMemberIdNullable(Long memberId) {
    return pgAccountReaderPort.retrievePGAccountOptionalByMemberId(memberId);
  }

  public PgAccount getPGAccountByMemberId(Long memberId) {
    return pgAccountReaderPort.retrievePGAccountByMemberId(memberId);
  }

  @AppTransactional
  public PgAccount registerPGAccount(Long memberId, String email, PgProviderType pgProvider) {
    ExtPGAccountDto extPGAccount =
        pgServiceProvider.getInstance(pgProvider).createNewAccount(memberId, email);

    PgAccount newPgAccount = PgAccount.newActivated(extPGAccount);

    return pgAccountStorePort.storeNew(newPgAccount);
  }
}
