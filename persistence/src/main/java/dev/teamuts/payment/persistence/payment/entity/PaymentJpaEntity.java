package dev.teamuts.payment.persistence.payment.entity;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.model.Payment;
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
@Table(catalog = "payment", name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PaymentJpaEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "service_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private AppServiceType serviceType;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  @Column(name = "pay_amount", nullable = false)
  private BigDecimal payAmount;

  @Column(name = "point_amount", nullable = false)
  private BigDecimal pointAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", columnDefinition = "CHAR(3)", nullable = false, length = 3)
  private Currency currency;

  @Column(name = "payment_method_id")
  private Long payMethodId;

  @Column(name = "payment_method_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentMethodType payMethodType;

  public static PaymentJpaEntity fromDomain(Payment model) {
    return PaymentJpaEntity.builder()
        .orderId(model.getOrderId())
        .memberId(model.getMemberId())
        .serviceType(model.getServiceType())
        .status(model.getStatus())
        .payAmount(model.getPayAmount())
        .pointAmount(model.getPointAmount())
        .currency(model.getCurrency())
        .payMethodId(model.getPayMethodId())
        .payMethodType(model.getPayMethodType())
        .build();
  }
}
