package dev.teamuts.payment.persistence.order.repository;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.persistence.order.entity.OrderJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface OrderRepository extends JpaRepository<OrderJpaEntity, Long> {

  Optional<OrderJpaEntity> findByExtOrderNumAndServiceType(
      String extOrderNum, AppServiceType serviceType);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<OrderJpaEntity> findLockById(Long id);
}
