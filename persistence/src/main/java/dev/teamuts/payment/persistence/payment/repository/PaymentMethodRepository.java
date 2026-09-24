package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.persistence.payment.entity.PaymentMethodJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodJpaEntity, Long> {
  List<PaymentMethodJpaEntity> findListByMemberId(Long memberId);
}
