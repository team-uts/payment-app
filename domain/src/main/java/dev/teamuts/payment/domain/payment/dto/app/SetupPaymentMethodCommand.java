package dev.teamuts.payment.domain.payment.dto.app;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;

public record SetupPaymentMethodCommand(Long memberId, String email, PgProviderType pgProvider) {}
