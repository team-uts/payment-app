package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;

public record PayOrderRequestDto(
    String extOrderNum,
    Long payMethodId,
    BigDecimal totalAmount,
    BigDecimal pointAmount,
    Currency currency,
    AppServiceType serviceType) {}
