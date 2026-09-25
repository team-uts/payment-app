package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
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
  private Card card;

  public static PaymentMethod fromDatabase(
      Long id,
      Long memberId,
      PGProviderType pgProvider,
      String providerToken,
      PaymentMethodType methodType,
      Boolean defaultMethod,
      PaymentMethodStatus status,
      Card card) {
    return PaymentMethod.builder()
        .id(id)
        .memberId(memberId)
        .pgProvider(pgProvider)
        .providerToken(providerToken)
        .methodType(methodType)
        .defaultMethod(defaultMethod)
        .status(status)
        .card(card)
        .build();
  }
}
