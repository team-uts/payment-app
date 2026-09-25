package dev.teamuts.payment.domain.payment.dto;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentMethodInfo {
  private Long id;
  private PGProviderType pgProvider;
  private PaymentMethodType methodType;
  private Boolean defaultMethod;
  private PaymentMethodStatus status;
  private CardDetailInfo card;

  public static PaymentMethodInfo of(PaymentMethod paymentMethod) {
    return PaymentMethodInfo.builder()
        .id(paymentMethod.getId())
        .pgProvider(paymentMethod.getPgProvider())
        .methodType(paymentMethod.getMethodType())
        .defaultMethod(paymentMethod.getDefaultMethod())
        .status(paymentMethod.getStatus())
        .card(CardDetailInfo.of(paymentMethod.getCard()))
        .build();
  }
}
