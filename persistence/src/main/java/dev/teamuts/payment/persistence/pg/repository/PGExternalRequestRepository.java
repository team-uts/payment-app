package dev.teamuts.payment.persistence.pg.repository;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.persistence.pg.entity.PGExternalRequestJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PGExternalRequestRepository
    extends JpaRepository<PGExternalRequestJpaEntity, Long> {

  List<PGExternalRequestJpaEntity> findAllByPgRequestIdAndMemberIdAndPgProvider(
      String pgRequestId, Long memberId, PGProviderType pgProvider);
}
