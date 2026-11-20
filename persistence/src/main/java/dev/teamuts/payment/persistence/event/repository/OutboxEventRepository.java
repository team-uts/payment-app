package dev.teamuts.payment.persistence.event.repository;

import dev.teamuts.payment.persistence.event.entity.OutboxEventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEventJpaEntity, Long> {}
