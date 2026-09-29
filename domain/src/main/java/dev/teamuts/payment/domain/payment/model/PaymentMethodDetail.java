package dev.teamuts.payment.domain.payment.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class PaymentMethodDetail {
  private Long payMethodId;
}
