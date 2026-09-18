package dev.teamuts.payment.persistence.pg.adapter;

import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestStorePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.pg.entity.PGExternalRequestJpaEntity;
import dev.teamuts.payment.persistence.pg.mapper.PGExternalRequestConverter;
import dev.teamuts.payment.persistence.pg.repository.PGExternalRequestRepository;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PGExternalRequestStoreAdapter implements PGExternalRequestStorePort {
  private final PGExternalRequestRepository pgExternalRequestRepository;
  private final PGExternalRequestConverter pgExternalRequestConverter;

  @Override
  public PGExternalRequest storeNew(PGExternalRequest pgExternalRequest) {
    PGExternalRequestJpaEntity newEntity =
        pgExternalRequestConverter.covertToJpaEntity(pgExternalRequest);

    PGExternalRequestJpaEntity savedEntity = pgExternalRequestRepository.save(newEntity);

    return pgExternalRequestConverter.covertToDomainModel(savedEntity);
  }
}
