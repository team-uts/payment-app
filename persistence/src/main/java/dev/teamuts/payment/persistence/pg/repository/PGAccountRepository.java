package dev.teamuts.payment.persistence.pg.repository;

import dev.teamuts.payment.persistence.pg.entity.PGAccountJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PGAccountRepository extends JpaRepository<PGAccountJpaEntity, Long> {
  Optional<PGAccountJpaEntity> findByMemberId(Long memberId);
}
