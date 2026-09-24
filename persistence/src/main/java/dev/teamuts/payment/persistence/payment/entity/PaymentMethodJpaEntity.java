package dev.teamuts.payment.persistence.payment.entity;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.persistence.common.entity.BaseAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(catalog = "payment", name = "payment_methods")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PaymentMethodJpaEntity extends BaseAuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "pg_provider", nullable = false)
  @Enumerated(EnumType.STRING)
  private PGProviderType pgProvider;

  @Column(name = "method_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentMethodType methodType;

  // TODO: Consider encrypting the provider token for security purposes
  @Column(name = "provider_token", nullable = false)
  private String providerToken;

  @Column(name = "is_default", nullable = false)
  private Boolean defaultMethod;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentMethodStatus status;
}
