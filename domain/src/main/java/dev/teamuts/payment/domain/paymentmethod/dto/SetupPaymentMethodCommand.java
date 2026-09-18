package dev.teamuts.payment.domain.paymentmethod.dto;

public record SetupPaymentMethodCommand(Long memberId, String email) {}
