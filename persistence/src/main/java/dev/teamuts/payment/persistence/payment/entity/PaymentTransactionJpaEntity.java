package dev.teamuts.payment.persistence.payment.entity;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
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
@Table(catalog = "payment", name = "payment_transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PaymentTransactionJpaEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "payment_id", nullable = false)
  private Long paymentId;

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(name = "payment_method_id", nullable = false)
  private Long payMethodId;

  @Column(name = "payment_method_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentMethodType payMethodType;

  @Column(name = "service_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private AppServiceType serviceType;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", columnDefinition = "CHAR(3)", nullable = false, length = 3)
  private Currency currency;

  @Column(name = "pg_provider", nullable = false)
  @Enumerated(EnumType.STRING)
  private PGProviderType pgProvider;

  @Column(name = "pg_transaction_id")
  private String pgTransactionId;

  @Column(name = "pg_response_message")
  private String pgResponseMessage;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentTransactionStatus status;

  public static PaymentTransactionJpaEntity newEntity(PaymentTransaction paymentTransaction) {
    return PaymentTransactionJpaEntity.builder()
        .memberId(paymentTransaction.getMemberId())
        .paymentId(paymentTransaction.getPaymentId())
        .orderId(paymentTransaction.getOrderId())
        .payMethodId(paymentTransaction.getPayMethodId())
        .payMethodType(paymentTransaction.getPayMethodType())
        .serviceType(paymentTransaction.getServiceType())
        .amount(paymentTransaction.getAmount().getAmount())
        .currency(paymentTransaction.getCurrency())
        .pgProvider(paymentTransaction.getPgProvider())
        .pgTransactionId(paymentTransaction.getPgTransactionId())
        .pgResponseMessage(paymentTransaction.getPgResponseMessage())
        .status(paymentTransaction.getStatus())
        .build();
  }
}
