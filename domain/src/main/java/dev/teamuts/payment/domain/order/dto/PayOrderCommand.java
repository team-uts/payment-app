package dev.teamuts.payment.domain.order.dto;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;

public record PayOrderCommand(
    String extOrderNum, BigDecimal amount, Currency currency, AppServiceType serviceType) {}
