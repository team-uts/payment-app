package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.model.PgAccount;

public interface PGAccountStorePort {
  PgAccount storeNew(PgAccount pgAccount);
}
