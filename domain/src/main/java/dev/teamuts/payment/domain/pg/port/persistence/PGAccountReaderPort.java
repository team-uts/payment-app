package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.model.PgAccount;
import java.util.Optional;

public interface PGAccountReaderPort {
  Optional<PgAccount> retrievePGAccountOptionalByMemberId(Long memberId);

  PgAccount retrievePGAccountByMemberId(Long memberId);
}
