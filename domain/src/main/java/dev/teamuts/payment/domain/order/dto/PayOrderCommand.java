package dev.teamuts.payment.domain.order.dto;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.shared.domain.money.Money;

public record PayOrderCommand(
    String extOrderNum,
    Long payMethodId,
    Money totalAmount,
    Money pointAmount,
    AppServiceType serviceType) {

  public boolean hasAnyPointUsage() {
    return pointAmount.isGreaterThanZero();
  }

  public boolean hasOnlyPointUsage() {
    return pointAmount.isGreaterThanZero() && totalAmount.isEqual(pointAmount);
  }
}
