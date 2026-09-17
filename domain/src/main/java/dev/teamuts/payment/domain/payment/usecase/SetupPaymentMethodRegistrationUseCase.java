package dev.teamuts.payment.domain.payment.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.paymentmethod.dto.PaymentMethodCommand;

@UseCase
public class SetupPaymentMethodRegistrationUseCase {
  public String execute(PaymentMethodCommand.SetupPaymentMethod command) {
    return "";
  }
}
