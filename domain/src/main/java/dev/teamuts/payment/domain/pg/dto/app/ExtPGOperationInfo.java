package dev.teamuts.payment.domain.pg.dto.app;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;

public class ExtPGOperationInfo {
  public record SetupPaymentMethodInfo(
      Long memberId, PgProviderType pgProvider, String providerToken) {
    public static SetupPaymentMethodInfo of(PGExternalRequest pgExternalRequest) {
      return new SetupPaymentMethodInfo(
          pgExternalRequest.getMemberId(),
          pgExternalRequest.getPgProvider(),
          pgExternalRequest.getProviderSecret());
    }
  }
}
