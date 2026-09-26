package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import lombok.Builder;
import lombok.Getter;

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

  @Getter
  @Builder
  public static class WebhookEventInfo {
    private PGProviderType pgProvider;
    private PGRequestType requestType;
    private Long memberId;
    private String pgRequestId;
    private String pgProviderToken; // e.g., PaymentMethod ID in SetupIntent
    private String pgOperationName;
    private String pgDetailedMessage;
  }
}
