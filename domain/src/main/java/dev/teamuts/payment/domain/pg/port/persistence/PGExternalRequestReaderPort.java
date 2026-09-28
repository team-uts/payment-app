package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import java.util.List;

public interface PGExternalRequestReaderPort {
  List<PGExternalRequest> retrievePGExternalRequestList(
      PGProviderType pgProvider, Long memberId, String pgRequestId);
}
