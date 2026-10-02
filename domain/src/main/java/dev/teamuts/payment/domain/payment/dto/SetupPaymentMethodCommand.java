package dev.teamuts.payment.domain.payment.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;

public record SetupPaymentMethodCommand(Long memberId, String email, PGProviderType pgProvider) {}
