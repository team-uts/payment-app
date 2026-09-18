package dev.teamuts.payment.domain.paymentmethod.command;

public record SetupPaymentMethodCommand(Long memberId, String email) {}
