package dev.teamuts.payment.persistence.pg.repository;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.persistence.pg.entity.PGExternalRequestJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PGExternalRequestRepository
    extends JpaRepository<PGExternalRequestJpaEntity, Long> {
  Optional<PGExternalRequestJpaEntity>
      findTopByPgRequestIdAndMemberIdAndPgProviderAndExtOperationOrderByIdDesc(
          String pgRequestId, Long memberId, PGProviderType pgProvider, String extOperation);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<PGExternalRequestJpaEntity> findLockById(Long id);

  Optional<PGExternalRequestJpaEntity>
      findTopByPgRequestIdAndMemberIdAndPgProviderAndExtOperationAndExtDetailedMessageOrderByIdDesc(
          String pgRequestId,
          Long memberId,
          PGProviderType pgProvider,
          String extOperation,
          String extDetailedMessage);
}
