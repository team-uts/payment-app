package dev.teamuts.payment.domain.order.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Order {
  private Long id;
  private String extOrderNum;
  private AppServiceType serviceType;
  private Long memberId;
  private BigDecimal amount;
  private Currency currency;
  private OrderStatus status;
}
