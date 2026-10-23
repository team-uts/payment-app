package dev.teamuts.payment.domain.order.dto;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;

public record PayOrderInfo(
    String extOrderNum,
    OrderStatus orderStatus,
    BigDecimal totalAmount,
    Currency currency,
    AppServiceType serviceType) {
  public static PayOrderInfo of(Order order) {
    return new PayOrderInfo(
        order.getExtOrderNum(),
        order.getStatus(),
        order.getAmount(),
        order.getCurrency(),
        order.getServiceType());
  }
}
