package dev.teamuts.payment.domain.order;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;

public class OrderFixtureFactory {
  private static final Long ORDER_ID = 1L;
  private static final Long MEMBER_ID = 100L;
  private static final String EXT_ORDER_NUM = "EXT-ORDER-0001";
  private static final Currency CURRENCY = Currency.KRW;
  private static final BigDecimal TOTAL_AMOUNT = BigDecimal.valueOf(10_000);

  public static Order createSingle(OrderStatus status) {
    return Order.fromDatabase(
        ORDER_ID, EXT_ORDER_NUM, AppServiceType.TAXI, MEMBER_ID, TOTAL_AMOUNT, CURRENCY, status);
  }
}
