package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;

public record PayOrderRequestDto(
    String extOrderNum,
    Long paymentMethodId,
    BigDecimal amount,
    Currency currency,
    AppServiceType serviceType) {}
