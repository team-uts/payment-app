package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;

public class PGExtOperationInfo {
  public record SetupPaymentMethodInfo(
      Long memberId, PGProviderType pgProvider, String providerToken) {
    public static SetupPaymentMethodInfo of(PGExternalRequest pgExternalRequest) {
      return new SetupPaymentMethodInfo(
          pgExternalRequest.getMemberId(),
          pgExternalRequest.getPgProvider(),
          pgExternalRequest.getProviderSecret());
    }
  }

  public record WebhookEventInfo(
      PGProviderType pgProvider,
      PGRequestType requestType,
      Long memberId,
      String pgRequestId,
      String pgProviderToken) {}
}
