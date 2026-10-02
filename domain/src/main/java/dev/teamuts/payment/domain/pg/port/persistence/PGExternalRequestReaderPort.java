package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import java.util.Optional;

public interface PGExternalRequestReaderPort {
  PGExternalRequest retrieveSingleByParameters(
      String pgRequestId, PGProviderType pgProvider, Long memberId, String extOperation);

  Optional<PGExternalRequest> retrieveSingleByParametersNullable(
      String pgRequestId,
      PGProviderType pgProvider,
      Long memberId,
      String extOperation,
      String extDetailedMessage);
}
