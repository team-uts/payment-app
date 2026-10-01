package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;

public interface PGExternalRequestReaderPort {
  PGExternalRequest retrieveSingleByParameters(
      PGProviderType pgProvider, Long memberId, String pgRequestId, String extOperation);
}
