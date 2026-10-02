package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.infra.provider.ExternalPGServiceProvider;
import dev.teamuts.payment.domain.pg.port.persistence.PGAccountReaderPort;
import dev.teamuts.payment.domain.pg.port.persistence.PGAccountStorePort;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PGAccountService {
  private final PGAccountReaderPort pgAccountReaderPort;
  private final PGAccountStorePort pgAccountStorePort;
  private final ExternalPGServiceProvider pgServiceProvider;

  public Optional<PGAccount> getPGAccountByMemberIdNullable(Long memberId) {
    return pgAccountReaderPort.retrievePGAccountOptionalByMemberId(memberId);
  }

  public PGAccount getPGAccountByMemberId(Long memberId) {
    return getPGAccountByMemberIdNullable(memberId)
        .orElseThrow(() -> new RuntimeException("PGAccount not found for memberId: " + memberId));
  }

  @AppTransactional
  public PGAccount registerPGAccount(Long memberId, String email, PGProviderType pgProvider) {
    ExtPGAccountDto extPGAccount =
        pgServiceProvider.getInstance(pgProvider).createNewAccount(memberId, email);

    PGAccount newPGAccount = PGAccount.newActivated(extPGAccount);

    return pgAccountStorePort.storeNew(newPGAccount);
  }
}
