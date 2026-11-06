package dev.teamuts.payment.domain.order.dto;

import dev.teamuts.payment.domain.common.constant.Currency;
import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.constant.AppServiceType;

public record PayOrderCommand(
    String extOrderNum,
    Long payMethodId,
    Money totalAmount,
    Money pointAmount,
    Currency currency,
    AppServiceType serviceType) {

  public boolean hasAnyPointUsage() {
    return pointAmount.isGreaterThanZero();
  }

  public boolean hasOnlyPointUsage() {
    return pointAmount.isGreaterThanZero() && totalAmount.isEqual(pointAmount);
  }
}
