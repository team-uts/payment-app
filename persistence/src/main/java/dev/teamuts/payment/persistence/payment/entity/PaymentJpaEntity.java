package dev.teamuts.payment.persistence.payment.entity;

import static dev.teamuts.shared.data.constant.DatabaseNaming.PREFIX_INDEX;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.persistence.common.entity.BaseTimeEntity;
import dev.teamuts.shared.domain.constant.Currency;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
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
    name = "payments",
    indexes = {
      @Index(name = PREFIX_INDEX + "_p_orderid", columnList = "order_id"),
      @Index(name = PREFIX_INDEX + "_p_memberid", columnList = "member_id"),
    })
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

  @Enumerated(EnumType.STRING)
  @Column(name = "service_type", nullable = false)
  private AppServiceType serviceType;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type", nullable = false)
  private PaymentType paymentType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
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

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method_type", nullable = false)
  private PaymentMethodType payMethodType;

  @Enumerated(EnumType.STRING)
  @Column(name = "pg_provider", nullable = false)
  private PgProviderType pgProvider;

  @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
  private List<PaymentTransactionJpaEntity> transactions;

  @Column(name = "outbox_event_id", comment = "associated outbox event ID")
  private Long outboxEventId;

  public static PaymentJpaEntity fromDomain(Payment model) {
    return PaymentJpaEntity.builder()
        .orderId(model.getOrderId())
        .memberId(model.getMemberId())
        .serviceType(model.getServiceType())
        .paymentType(model.getPaymentType())
        .status(model.getStatus())
        .payAmount(model.getPayAmount().getAmount())
        .pointAmount(model.getPointAmount().getAmount())
        .currency(model.getCurrency())
        .payMethodId(model.getPayMethodId())
        .payMethodType(model.getPayMethodType())
        .pgProvider(model.getPgProvider())
        .outboxEventId(model.getOutboxEventId())
        .build();
  }

  public void updateStatus(PaymentStatus status) {
    this.status = status;
  }

  public void updateOutboxEventId(Long outboxEventId) {
    this.outboxEventId = outboxEventId;
  }
}
