package dev.teamuts.payment.domain.pg.dto.infra;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtPGPaymentMethodOperationDto {
  private Long memberId;
  private String pgOperationId;
  private String pgOperationName;
  private String pgAccountId;
  private PgProviderType pgProvider;
  private String pgProviderSecret;
}
