package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.pg.constant.PGAccountStatus;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PGAccount {
  private Long id;
  private Long memberId;
  private String pgAccountId;
  private PGProviderType pgProvider;
  private PGAccountStatus status;

  public static PGAccount activateNew(Long memberId, String pgAccountId, PGProviderType provider) {
    return PGAccount.builder()
        .memberId(memberId)
        .pgAccountId(pgAccountId)
        .pgProvider(provider)
        .status(PGAccountStatus.ACTIVATED)
        .build();
  }

  public static PGAccount fromDatabase(
      Long id,
      Long memberId,
      String pgAccountId,
      PGProviderType pgProvider,
      PGAccountStatus status) {
    return PGAccount.builder()
        .id(id)
        .memberId(memberId)
        .pgAccountId(pgAccountId)
        .pgProvider(pgProvider)
        .status(status)
        .build();
  }
}
