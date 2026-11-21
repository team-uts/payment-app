package dev.teamuts.payment.persistence.pg.adapter;

import dev.teamuts.payment.domain.pg.model.PgAccount;
import dev.teamuts.payment.domain.pg.port.persistence.PGAccountStorePort;
import dev.teamuts.payment.persistence.pg.entity.PGAccountJpaEntity;
import dev.teamuts.payment.persistence.pg.mapper.PGAccountConverter;
import dev.teamuts.payment.persistence.pg.repository.PGAccountRepository;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PGAccountStoreAdapter implements PGAccountStorePort {
  private final PGAccountRepository pgAccountRepository;
  private final PGAccountConverter pgAccountConverter;

  @Override
  public PgAccount storeNew(PgAccount pgAccount) {
    PGAccountJpaEntity newEntity = pgAccountConverter.covertToJpaEntity(pgAccount);

    PGAccountJpaEntity savedEntity = pgAccountRepository.save(newEntity);

    return pgAccountConverter.covertToDomainModel(savedEntity);
  }
}
