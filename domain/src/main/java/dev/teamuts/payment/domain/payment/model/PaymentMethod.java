package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class PaymentMethod {
  private Long id;
  private Long memberId;
  private PGProviderType pgProvider;
  private String providerToken;
  private PaymentMethodType methodType;
  private Boolean defaultMethod;
  private PaymentMethodStatus status;
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

  public static PaymentMethod copyWithDetail(PaymentMethod origin, PaymentMethodDetail newDetail) {
    return PaymentMethod.builder()
        .id(origin.getId())
        .memberId(origin.getMemberId())
        .pgProvider(origin.getPgProvider())
        .providerToken(origin.getProviderToken())
        .methodType(origin.getMethodType())
        .defaultMethod(origin.getDefaultMethod())
        .status(origin.getStatus())
        .detail(newDetail)
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
    return PaymentMethod.builder()
        .id(id)
        .memberId(memberId)
        .pgProvider(pgProvider)
        .providerToken(providerToken)
        .methodType(methodType)
        .defaultMethod(defaultMethod)
        .status(status)
        .detail(detail)
        .build();
  }

  public boolean isSameMethodType(PaymentMethodType otherType) {
    return this.methodType == otherType;
  }
}
