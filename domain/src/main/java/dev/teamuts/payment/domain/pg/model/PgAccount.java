package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.pg.constant.PgAccountStatus;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGAccountDto;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.constant.ModelSourceType;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Getter
public class PgAccount extends BaseIdentifiableModel<Long> {
  private Long memberId;
  private String pgAccountId;
  private PgProviderType pgProvider;
  private PgAccountStatus status;

  @Builder(access = AccessLevel.PRIVATE, toBuilder = true)
  private PgAccount(
      @Builder.ObtainVia(method = "getId") Long id,
      @Builder.ObtainVia(method = "getSourceType") ModelSourceType sourceType,
      Long memberId,
      String pgAccountId,
      PgProviderType pgProvider,
      PgAccountStatus status) {
    super(id, sourceType);
    this.memberId = memberId;
    this.pgAccountId = pgAccountId;
    this.pgProvider = pgProvider;
    this.status = status;
  }

  public static PgAccount newActivated(ExtPGAccountDto extPGAccount) {
    return PgAccount.builder()
        .sourceType(ModelSourceType.LOGIC)
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
    return PgAccount.builder()
        .id(id)
        .sourceType(ModelSourceType.DATABASE)
        .memberId(memberId)
        .pgAccountId(pgAccountId)
        .pgProvider(pgProvider)
        .status(status)
        .build();
  }
}
