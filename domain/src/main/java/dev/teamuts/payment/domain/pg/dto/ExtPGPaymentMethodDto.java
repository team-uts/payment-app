package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtPGPaymentMethodDto {
  private String pgPaymentMethodId;
  private PGProviderType pgProvider;
  private PaymentMethodType methodType;

  @Getter
  @Builder
  private static class ExtPGCardDto {
    private String brand;
    private String last4;
    private Integer expMonth;
    private Integer expYear;
  }

  /**
   * TODO: Add more fields for bank account details if needed. In this demonstration, we are not
   * going to use bank account details.
   */
  private static class ExtPGBankAccountDto {
    private String bankName;
    private String accountNumber;
    private String routingNumber;
  }
}
