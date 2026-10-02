package dev.teamuts.payment.persistence.pg.repository.custom;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;

public interface PGExternalRequestQueryRepository {
  Long findTheLatestIdByPgRequestIdAndMemberIdAndPgProviderAndExtOperation(
      String pgRequestId, Long memberId, PGProviderType pgProvider, String extOperation);
}
