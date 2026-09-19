package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.model.PGAccount;

public interface PGAccountStorePort {
  PGAccount storeNew(PGAccount pgAccount);
}
