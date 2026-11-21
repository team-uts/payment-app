package dev.teamuts.payment.persistence.event.repository;

import dev.teamuts.payment.persistence.event.entity.OutboxEventJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface OutboxEventRepository extends JpaRepository<OutboxEventJpaEntity, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<OutboxEventJpaEntity> findLockById(Long id);
}
