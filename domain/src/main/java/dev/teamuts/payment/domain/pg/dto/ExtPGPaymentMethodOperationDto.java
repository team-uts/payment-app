package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtPGPaymentMethodOperationDto {
  private Long memberId;
  private String pgOperationId;
  private String pgOperationName;
  private String pgAccountId;
  private PGProviderType pgProvider;
  private String pgProviderSecret;
}
