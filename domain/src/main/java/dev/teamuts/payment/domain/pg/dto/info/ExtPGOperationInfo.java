package dev.teamuts.payment.domain.pg.dto.info;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;

public class ExtPGOperationInfo {
  public record SetupPaymentMethodInfo(
      Long memberId, PGProviderType pgProvider, String providerToken) {
    public static SetupPaymentMethodInfo of(PGExternalRequest pgExternalRequest) {
      return new SetupPaymentMethodInfo(
          pgExternalRequest.getMemberId(),
          pgExternalRequest.getPgProvider(),
          pgExternalRequest.getProviderSecret());
    }
  }
}
