package dev.teamuts.payment.persistence.pg.repository.custom;

import static dev.teamuts.payment.persistence.pg.entity.QPGExternalRequestJpaEntity.pGExternalRequestJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PGExternalRequestQueryRepositoryImpl implements PGExternalRequestQueryRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Long findTheLatestIdByPgRequestIdAndMemberIdAndPgProviderAndExtOperation(
      String pgRequestId, Long memberId, PGProviderType pgProvider, String extOperation) {
    return jpaQueryFactory
        .select(pGExternalRequestJpaEntity.id)
        .from(pGExternalRequestJpaEntity)
        .where(
            pGExternalRequestJpaEntity.pgRequestId.eq(pgRequestId),
            pGExternalRequestJpaEntity.memberId.eq(memberId),
            pGExternalRequestJpaEntity.pgProvider.eq(pgProvider),
            pGExternalRequestJpaEntity.extOperation.eq(extOperation))
        .orderBy(pGExternalRequestJpaEntity.id.desc())
        .fetchOne();
  }
}
