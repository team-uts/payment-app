package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.common.model.BaseIdentifiableModel;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class PaymentMethod extends BaseIdentifiableModel<Long> {
  private Long memberId;
  private PGProviderType pgProvider;
  private String providerToken;
  private PaymentMethodType methodType;
  private Boolean defaultMethod;
  private PaymentMethodStatus status;

  @Getter(AccessLevel.NONE)
  private PaymentMethodDetail detail;

  public static PaymentMethod newActiveMethod(ExtPGPaymentMethodDto extPGPaymentMethod) {
    PaymentMethodBuilder builder =
        PaymentMethod.builder()
            .memberId(extPGPaymentMethod.getMemberId())
            .pgProvider(extPGPaymentMethod.getPgProvider())
            .providerToken(extPGPaymentMethod.getPgPaymentMethodId())
            .methodType(extPGPaymentMethod.getDetail().methodType())
            .defaultMethod(true) // TODO: Set defaultMethod based on business logic
            .status(PaymentMethodStatus.ACTIVE);

    return builder.build();
  }

  public static PaymentMethod point(Long memberId) {
    return PaymentMethod.builder()
        .memberId(memberId)
        .pgProvider(PGProviderType.NONE)
        .providerToken(null)
        .methodType(PaymentMethodType.POINT)
        .defaultMethod(false)
        .status(PaymentMethodStatus.ACTIVE)
        .build();
  }

  public static PaymentMethod fromDatabase(
      Long id,
      Long memberId,
      PGProviderType pgProvider,
      String providerToken,
      PaymentMethodType methodType,
      Boolean defaultMethod,
      PaymentMethodStatus status,
      PaymentMethodDetail detail) {
    PaymentMethod pm =
        PaymentMethod.builder()
            .memberId(memberId)
            .pgProvider(pgProvider)
            .providerToken(providerToken)
            .methodType(methodType)
            .defaultMethod(defaultMethod)
            .status(status)
            .detail(detail)
            .build();

    pm.id = id;

    return pm;
  }

  public void updateDetail(PaymentMethodDetail newDetail) {
    this.detail = newDetail;
  }

  // Return copy
  public PaymentMethodDetail getDetailOrNull() {
    if (detail instanceof Card cardDetail) {
      return cardDetail.copy();
    }

    return null;
  }

  public void checkIfBelongsToMember(Long memberId) {
    if (!this.memberId.equals(memberId)) {
      throw new IllegalArgumentException(
          "PaymentMethod does not belong to the member (memberId: %s)".formatted(memberId));
    }
  }

  public void checkIfActive() {
    if (this.status != PaymentMethodStatus.ACTIVE) {
      throw new IllegalStateException("PaymentMethod (id: %s) is not active".formatted(this.id));
    }
  }

  public boolean isPointType() {
    return this.methodType == PaymentMethodType.POINT;
  }
}
