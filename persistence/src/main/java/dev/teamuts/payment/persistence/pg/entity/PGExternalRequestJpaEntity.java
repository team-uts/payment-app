package dev.teamuts.payment.persistence.pg.entity;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestStatus;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.persistence.common.entity.BaseAccountableEntity;
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
@Table(catalog = "payment", name = "pg_external_requests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PGExternalRequestJpaEntity extends BaseAccountableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "pg_request_id", nullable = false, unique = true)
  private String pgRequestId;

  @Column(name = "member_id", nullable = false, unique = true)
  private Long memberId;

  @Column(name = "pg_provider", nullable = false)
  @Enumerated(EnumType.STRING)
  private PGProviderType pgProvider;

  @Column(name = "request_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private PGRequestType requestType;

  @Column(name = "provider_secret", nullable = false)
  private String providerSecret;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PGRequestStatus status;

  public static PGExternalRequestJpaEntity newEntity(
      String pgRequestId,
      Long memberId,
      PGProviderType pgProvider,
      PGRequestType requestType,
      String providerSecret,
      PGRequestStatus status) {
    return PGExternalRequestJpaEntity.builder()
        .pgRequestId(pgRequestId)
        .memberId(memberId)
        .pgProvider(pgProvider)
        .requestType(requestType)
        .providerSecret(providerSecret)
        .status(status)
        .build();
  }
}
