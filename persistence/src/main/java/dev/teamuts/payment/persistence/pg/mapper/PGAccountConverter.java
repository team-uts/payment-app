package dev.teamuts.payment.persistence.pg.mapper;

import dev.teamuts.payment.domain.pg.model.PgAccount;
import dev.teamuts.payment.persistence.pg.entity.PGAccountJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PGAccountConverter {
  public PgAccount covertToDomainModel(PGAccountJpaEntity entity) {
    return PgAccount.fromDatabase(
        entity.getId(),
        entity.getMemberId(),
        entity.getPgAccountId(),
        entity.getPgProvider(),
        entity.getStatus());
  }

  public PGAccountJpaEntity covertToJpaEntity(PgAccount model) {
    return PGAccountJpaEntity.newEntity(
        model.getMemberId(), model.getPgAccountId(), model.getPgProvider(), model.getStatus());
  }
}
