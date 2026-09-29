package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.model.PGExternalRequest;

public interface PGExternalRequestUpdatePort {
  void updateStatus(PGExternalRequest pgExternalRequest);
}
