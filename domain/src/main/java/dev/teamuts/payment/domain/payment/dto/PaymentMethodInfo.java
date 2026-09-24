package dev.teamuts.payment.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentMethodInfo {
  private String pgMethodId;
  private String last4;
  private String brand;
  private String expMonth;
  private String expYear;
}
