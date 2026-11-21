package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;

// TODO: memberId, email << should be retrieved from the authentication token.
public record SetupPaymentMethodRequestDto(
    Long memberId, String email, PgProviderType pgProvider) {}
