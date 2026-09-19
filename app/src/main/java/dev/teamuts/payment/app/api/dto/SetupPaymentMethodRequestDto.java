package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;

public record SetupPaymentMethodRequestDto(
    Long memberId, String email, PGProviderType pgProvider) {}
