package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtPGAccountDto {
  private Long memberId;
  private String pgAccountId;
  private PGProviderType pgProvider;
}
