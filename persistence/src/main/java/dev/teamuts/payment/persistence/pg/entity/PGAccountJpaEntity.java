package dev.teamuts.payment.persistence.pg.entity;

import dev.teamuts.payment.domain.pg.constant.PGAccountStatus;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
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
@Table(catalog = "payment", name = "pg_accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PGAccountJpaEntity extends BaseAccountableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false, unique = true)
  private Long memberId;

  @Column(name = "pg_account_id", nullable = false, unique = true)
  private String pgAccountId;

  @Column(name = "pg_provider", nullable = false)
  @Enumerated(EnumType.STRING)
  private PGProviderType pgProvider;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PGAccountStatus status;

  public static PGAccountJpaEntity newEntity(
      Long memberId, String pgAccountId, PGProviderType pgProvider, PGAccountStatus status) {
    return PGAccountJpaEntity.builder()
        .memberId(memberId)
        .pgAccountId(pgAccountId)
        .pgProvider(pgProvider)
        .status(status)
        .build();
  }
}
