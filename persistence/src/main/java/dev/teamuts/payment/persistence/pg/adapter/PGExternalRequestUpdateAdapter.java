package dev.teamuts.payment.persistence.pg.adapter;

import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestUpdatePort;
import dev.teamuts.payment.persistence.pg.entity.PGExternalRequestJpaEntity;
import dev.teamuts.payment.persistence.pg.repository.PGExternalRequestRepository;
import dev.teamuts.shared.data.AppTransactional;
import dev.teamuts.shared.data.PersistenceException;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@AppTransactional
@PersistenceAdapter
@RequiredArgsConstructor
public class PGExternalRequestUpdateAdapter implements PGExternalRequestUpdatePort {
  private final PGExternalRequestRepository pgExternalRequestRepository;

  @Override
  public void updateStatus(PGExternalRequest pgExternalRequest) {
    PGExternalRequestJpaEntity entity =
        pgExternalRequestRepository
            .findLockById(pgExternalRequest.getId())
            .orElseThrow(() -> PersistenceException.notFound(pgExternalRequest));

    entity.updateStatus(pgExternalRequest.getStatus());
  }

  @Override
  public void updateSourceRequestIdAndStatus(PGExternalRequest pgExternalRequest) {
    PGExternalRequestJpaEntity entity =
        pgExternalRequestRepository
            .findLockById(pgExternalRequest.getId())
            .orElseThrow(() -> PersistenceException.notFound(pgExternalRequest));

    entity.updateStatus(pgExternalRequest.getStatus());
    entity.updateSourceRequestId(pgExternalRequest.getSourceRequestId());
  }
}
