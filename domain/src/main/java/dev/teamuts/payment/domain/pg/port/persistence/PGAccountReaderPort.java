package dev.teamuts.payment.domain.pg.port.persistence;

import dev.teamuts.payment.domain.pg.model.PGAccount;
import java.util.Optional;

public interface PGAccountReaderPort {
  Optional<PGAccount> retrievePGAccountOptionalByMemberId(Long memberId);;
}
