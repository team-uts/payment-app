package dev.teamuts.payment.persistence.pg.adapter;

import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.persistence.PGAccountReaderPort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.pg.mapper.PGAccountConverter;
import dev.teamuts.payment.persistence.pg.repository.PGAccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PGAccountReaderAdapter implements PGAccountReaderPort {
  private final PGAccountRepository pgAccountRepository;
  private final PGAccountConverter pgAccountConverter;

  @Override
  public Optional<PGAccount> retrievePGAccountOptionalByMemberId(Long memberId) {
    return pgAccountRepository
        .findByMemberId(memberId)
        .map(pgAccountConverter::covertToDomainModel);
  }
}
