package dev.teamuts.payment.persistence.pg.adapter;

import dev.teamuts.payment.domain.pg.exception.PgAccountErrorCode;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.persistence.PGAccountReaderPort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.pg.mapper.PGAccountConverter;
import dev.teamuts.payment.persistence.pg.repository.PGAccountRepository;
import dev.teamuts.payment.shared.error.exception.BusinessException;
import dev.teamuts.payment.shared.error.exception.ErrorArguments;
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

  @Override
  public PGAccount retrievePGAccountByMemberId(Long memberId) {
    return pgAccountRepository
        .findByMemberId(memberId)
        .map(pgAccountConverter::covertToDomainModel)
        .orElseThrow(
            () ->
                BusinessException.of(
                    PgAccountErrorCode.NOT_FOUND_WITH_MEMBER, ErrorArguments.of(memberId)));
  }
}
