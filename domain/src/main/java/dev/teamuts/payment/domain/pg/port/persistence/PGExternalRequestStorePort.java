package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.model.PGExternalRequest;

public interface PGExternalRequestStorePort {
  PGExternalRequest storeNew(PGExternalRequest pgExternalRequest);
}
