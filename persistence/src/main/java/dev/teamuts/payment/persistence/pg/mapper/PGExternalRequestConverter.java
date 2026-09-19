package dev.teamuts.payment.persistence.pg.mapper;

import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.persistence.pg.entity.PGExternalRequestJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PGExternalRequestConverter {
  public PGExternalRequest covertToDomainModel(PGExternalRequestJpaEntity entity) {
    return PGExternalRequest.fromDatabase(
        entity.getId(),
        entity.getPgRequestId(),
        entity.getMemberId(),
        entity.getPgProvider(),
        entity.getRequestType(),
        entity.getExtOperation(),
        entity.getExtProviderSecret(),
        entity.getStatus());
  }

  public PGExternalRequestJpaEntity covertToJpaEntity(PGExternalRequest model) {
    return PGExternalRequestJpaEntity.newEntity(
        model.getPgRequestId(),
        model.getMemberId(),
        model.getPgProvider(),
        model.getRequestType(),
        model.getExtOperation(),
        model.getProviderSecret(),
        model.getStatus());
  }
}
