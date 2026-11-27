package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import java.util.Optional;

public interface PGExternalRequestReaderPort {
  PGExternalRequest retrieveSingleByParameters(
      String pgRequestId, PgProviderType pgProvider, Long memberId, String extOperation);

  Optional<PGExternalRequest> retrieveSingleOrNullByParameters(
      String pgRequestId,
      PgProviderType pgProvider,
      Long memberId,
      String extOperation,
      String extDetailedMessage);
}
