package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentJpaEntity, Long> {
  Optional<PaymentJpaEntity> findByOrderIdAndServiceTypeAndPayMethodType(
      Long orderId, AppServiceType serviceType, PaymentMethodType payMethodType);
}
