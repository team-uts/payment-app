package dev.teamuts.payment.domain.paymentmethod.dto;

public class PaymentMethodCommand {
  public record SetupPaymentMethod(String memberId) {}
}
