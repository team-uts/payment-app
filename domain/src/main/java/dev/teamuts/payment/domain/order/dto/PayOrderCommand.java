package dev.teamuts.payment.domain.order.dto;

import dev.teamuts.shared.domain.model.Money;

public record PayOrderCommand(
    String extOrderNum, Long payMethodId, Money totalAmount, Money pointAmount) {

  public boolean hasAnyPointUsage() {
    return pointAmount.isGreaterThanZero();
  }

  public boolean hasOnlyPointUsage() {
    return pointAmount.isGreaterThanZero() && totalAmount.isEqual(pointAmount);
  }
}
