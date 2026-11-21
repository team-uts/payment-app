package dev.teamuts.payment.domain.pg.dto.infra;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtPGAccountDto {
  private Long memberId;
  private String pgAccountId;
  private PgProviderType pgProvider;
}
