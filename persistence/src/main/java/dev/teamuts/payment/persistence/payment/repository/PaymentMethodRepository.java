package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.persistence.payment.entity.PaymentMethodJpaEntity;
import dev.teamuts.payment.persistence.payment.repository.custom.PaymentMethodQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository
    extends JpaRepository<PaymentMethodJpaEntity, Long>, PaymentMethodQueryRepository {
  // TODO: it needs HMAC-SHA-256 hash value in the table for lookup using provider_token.
  boolean existsByMemberIdAndProviderTokenAndPgProvider(
      Long memberId, String providerToken, PGProviderType pgProvider);
}
