package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.pg.constant.PgAccountStatus;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGAccountDto;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class PgAccount extends BaseIdentifiableModel<Long> {
  private Long memberId;
  private String pgAccountId;
  private PgProviderType pgProvider;
  private PgAccountStatus status;

  public static PgAccount newActivated(ExtPGAccountDto extPGAccount) {
    return PgAccount.builder()
        .memberId(extPGAccount.getMemberId())
        .pgAccountId(extPGAccount.getPgAccountId())
        .pgProvider(extPGAccount.getPgProvider())
        .status(PgAccountStatus.ACTIVATED)
        .build();
  }

  public static PgAccount fromDatabase(
      Long id,
      Long memberId,
      String pgAccountId,
      PgProviderType pgProvider,
      PgAccountStatus status) {
    PgAccount pga =
        PgAccount.builder()
            .memberId(memberId)
            .pgAccountId(pgAccountId)
            .pgProvider(pgProvider)
            .status(status)
            .build();

    pga.setId(id);

    return pga;
  }
}
