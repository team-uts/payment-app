package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.common.constant.Currency;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import java.math.BigDecimal;

public record PayOrderRequestDto(
    String extOrderNum,
    Long payMethodId,
    BigDecimal totalAmount,
    BigDecimal pointAmount,
    Currency currency,
    AppServiceType serviceType) {}
