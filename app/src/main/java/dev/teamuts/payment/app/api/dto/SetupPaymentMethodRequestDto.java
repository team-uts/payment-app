package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;

// TODO: memberId, email << should be retrieved from the authentication token.
public record SetupPaymentMethodRequestDto(
    Long memberId, String email, PGProviderType pgProvider) {}
