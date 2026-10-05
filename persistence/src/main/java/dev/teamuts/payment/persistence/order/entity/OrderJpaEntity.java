package dev.teamuts.payment.persistence.order.entity;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.persistence.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(catalog = "payment", name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class OrderJpaEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ext_order_no", nullable = false)
  private String extOrderNum;

  @Column(name = "service_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private AppServiceType serviceType;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", columnDefinition = "CHAR(3)", nullable = false, length = 3)
  private Currency currency;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public static OrderJpaEntity fromDomain(Order order) {
    return OrderJpaEntity.builder()
        .id(order.getId())
        .extOrderNum(order.getExtOrderNum())
        .serviceType(order.getServiceType())
        .memberId(order.getMemberId())
        .amount(order.getAmount())
        .currency(order.getCurrency())
        .status(order.getStatus())
        .build();
  }
}
