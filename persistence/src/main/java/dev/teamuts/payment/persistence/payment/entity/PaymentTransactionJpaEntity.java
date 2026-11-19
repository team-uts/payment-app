package dev.teamuts.payment.persistence.payment.entity;

import static dev.teamuts.shared.data.constant.DatabaseNaming.PREFIX_INDEX;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
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
    name = "payment_transactions",
    indexes = {
      @Index(name = PREFIX_INDEX + "_pt_memberid", columnList = "member_id"),
      @Index(name = PREFIX_INDEX + "_pt_paymentid", columnList = "payment_id"),
      @Index(name = PREFIX_INDEX + "_pt_orderid", columnList = "order_id"),
    })
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

  @Column(name = "payment_method_id")
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

  @Column(name = "pg_response_code")
  private String pgResponseCode;

  @Column(name = "pg_error_code")
  private String pgErrorCode;

  @Column(name = "pg_error_message")
  private String pgErrorMessage;

  @Column(name = "detail_message")
  private String detailMessage;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentTransactionStatus status;

  @Column(name = "seq", nullable = false)
  private Integer sequence;

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
        .pgResponseCode(paymentTransaction.getPgResponseCode())
        .pgErrorCode(paymentTransaction.getPgErrorCode())
        .pgErrorMessage(paymentTransaction.getPgErrorMessage())
        .detailMessage(paymentTransaction.getDetailMessage())
        .status(paymentTransaction.getStatus())
        .sequence(paymentTransaction.getSequenceType().getSeqNumber())
        .build();
  }

  public void updatePGTransactionInfo(
      String pgTransactionId,
      String pgResponseCode,
      String pgErrorCode,
      String pgErrorMessage,
      String detailMessage,
      PaymentTransactionStatus status) {
    this.pgTransactionId = pgTransactionId;
    this.pgResponseCode = pgResponseCode;
    this.pgErrorCode = pgErrorCode;
    this.pgErrorMessage = pgErrorMessage;
    this.detailMessage = detailMessage;
    this.status = status;
  }

  public void updateDetailMessage(String detailMessage) {
    this.detailMessage = detailMessage;
  }
}
