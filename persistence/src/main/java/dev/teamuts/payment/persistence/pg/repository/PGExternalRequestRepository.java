package dev.teamuts.payment.persistence.pg.repository;

import dev.teamuts.payment.persistence.pg.entity.PGExternalRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PGExternalRequestRepository
    extends JpaRepository<PGExternalRequestJpaEntity, Long> {}
