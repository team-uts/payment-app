package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestStatus;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class PGExternalRequest {
  private Long id;
  private String pgRequestId;
  private Long memberId;
  private PGProviderType pgProvider;
  private PGRequestType requestType;
  private String providerSecret;
  private PGRequestStatus status;

  public static PGExternalRequest initPaymentMethodSetup(
      ExtPGPaymentMethodOperationDto paymentMethodOperation) {
    return PGExternalRequest.builder()
        .pgRequestId(paymentMethodOperation.getPgOperationId())
        .memberId(paymentMethodOperation.getMemberId())
        .pgProvider(paymentMethodOperation.getPgProvider())
        .requestType(PGRequestType.PAYMENT_METHOD)
        .providerSecret(paymentMethodOperation.getPgProviderSecret())
        .status(PGRequestStatus.INIT)
        .build();
  }

  public static PGExternalRequest fromDatabase(
      Long id,
      String pgRequestId,
      Long memberId,
      PGProviderType pgProvider,
      PGRequestType requestType,
      String providerSecret,
      PGRequestStatus status) {
    return PGExternalRequest.builder()
        .id(id)
        .pgRequestId(pgRequestId)
        .memberId(memberId)
        .pgProvider(pgProvider)
        .requestType(requestType)
        .providerSecret(providerSecret)
        .status(status)
        .build();
  }
}
