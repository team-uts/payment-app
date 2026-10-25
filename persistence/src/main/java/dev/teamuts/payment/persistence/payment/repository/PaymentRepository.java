package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PaymentRepository extends JpaRepository<PaymentJpaEntity, Long> {
  Optional<PaymentJpaEntity> findByOrderIdAndServiceTypeAndPayMethodType(
      Long orderId, AppServiceType serviceType, PaymentMethodType payMethodType);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<PaymentJpaEntity> findLockById(Long id);
}
