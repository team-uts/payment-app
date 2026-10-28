package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.BaseIdentifiableModel;
import dev.teamuts.payment.domain.pg.constant.PGAccountStatus;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGAccountDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class PGAccount extends BaseIdentifiableModel<Long> {
  private Long memberId;
  private String pgAccountId;
  private PGProviderType pgProvider;
  private PGAccountStatus status;

  public static PGAccount newActivated(ExtPGAccountDto extPGAccount) {
    return PGAccount.builder()
        .memberId(extPGAccount.getMemberId())
        .pgAccountId(extPGAccount.getPgAccountId())
        .pgProvider(extPGAccount.getPgProvider())
        .status(PGAccountStatus.ACTIVATED)
        .build();
  }

  public static PGAccount fromDatabase(
      Long id,
      Long memberId,
      String pgAccountId,
      PGProviderType pgProvider,
      PGAccountStatus status) {
    PGAccount pga =
        PGAccount.builder()
            .memberId(memberId)
            .pgAccountId(pgAccountId)
            .pgProvider(pgProvider)
            .status(status)
            .build();

    pga.id = id;

    return pga;
  }
}
