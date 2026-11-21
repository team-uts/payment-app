package dev.teamuts.payment.persistence.order.entity;

import static dev.teamuts.shared.data.constant.DatabaseNaming.PREFIX_INDEX;
import static dev.teamuts.shared.data.constant.DatabaseNaming.PREFIX_UNIQUE;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.persistence.common.entity.BaseTimeEntity;
import dev.teamuts.shared.domain.constant.Currency;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(
    catalog = "payment",
    name = "orders",
    uniqueConstraints = {
      @UniqueConstraint(
          name = PREFIX_UNIQUE + "_o_extorderno_servicetype",
          columnNames = {"ext_order_no", "service_type"})
    },
    indexes = {
      @Index(name = PREFIX_INDEX + "_memberid", columnList = "member_id"),
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class OrderJpaEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(
      name = "ext_order_no",
      nullable = false,
      updatable = false,
      comment = "external order number")
  private String extOrderNum;

  @Column(name = "service_type", nullable = false, updatable = false, comment = "app service type")
  @Enumerated(EnumType.STRING)
  private AppServiceType serviceType;

  @Column(name = "member_id", nullable = false, updatable = false)
  private Long memberId;

  @Column(name = "amount", nullable = false, updatable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", columnDefinition = "CHAR(3)", nullable = false, length = 3)
  private Currency currency;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private OrderStatus status;

  public static OrderJpaEntity fromDomain(Order order) {
    return OrderJpaEntity.builder()
        .id(order.getId())
        .extOrderNum(order.getExtOrderNum())
        .serviceType(order.getServiceType())
        .memberId(order.getMemberId())
        .amount(order.getAmount().getAmount())
        .currency(order.getCurrency())
        .status(order.getStatus())
        .build();
  }

  public void updateStatus(OrderStatus status) {
    this.status = status;
  }
}
