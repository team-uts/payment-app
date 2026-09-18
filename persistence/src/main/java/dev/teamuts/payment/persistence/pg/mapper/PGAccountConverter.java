package dev.teamuts.payment.persistence.pg.mapper;

import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.persistence.pg.entity.PGAccountJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PGAccountConverter {
  public PGAccount covertToDomainModel(PGAccountJpaEntity entity) {
    return PGAccount.fromDatabase(
        entity.getId(),
        entity.getMemberId(),
        entity.getPgAccountId(),
        entity.getPgProvider(),
        entity.getStatus());
  }
}
