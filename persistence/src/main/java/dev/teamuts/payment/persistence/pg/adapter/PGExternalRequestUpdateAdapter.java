package dev.teamuts.payment.persistence.pg.adapter;

import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestUpdatePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.pg.entity.PGExternalRequestJpaEntity;
import dev.teamuts.payment.persistence.pg.repository.PGExternalRequestRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
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
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "PGExternalRequest not found with id: " + pgExternalRequest.getId()));

    entity.updateStatus(pgExternalRequest.getStatus());
  }

  @Override
  public void updateSourceRequestIdAndStatus(PGExternalRequest pgExternalRequest) {
    PGExternalRequestJpaEntity entity =
        pgExternalRequestRepository
            .findLockById(pgExternalRequest.getId())
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "PGExternalRequest not found with id: " + pgExternalRequest.getId()));

    entity.updateStatus(pgExternalRequest.getStatus());
    entity.updateSourceRequestId(pgExternalRequest.getSourceRequestId());
  }
}
