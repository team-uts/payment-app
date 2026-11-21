package dev.teamuts.payment.persistence.pg.repository.custom;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;

public interface PGExternalRequestQueryRepository {
  Long findTheLatestIdByPgRequestIdAndMemberIdAndPgProviderAndExtOperation(
      String pgRequestId, Long memberId, PgProviderType pgProvider, String extOperation);
}
