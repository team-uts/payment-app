package dev.teamuts.payment.domain.order.dto;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.shared.domain.model.Money;

public record PayOrderCommand(
    String extOrderNum,
    AppServiceType serviceType,
    Long payMethodId,
    Money totalAmount,
    Money pointAmount) {}
