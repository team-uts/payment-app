package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.persistence.payment.entity.PaymentTransactionJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PaymentTransactionRepository
    extends JpaRepository<PaymentTransactionJpaEntity, Long> {
  List<PaymentTransactionJpaEntity> findAllByPaymentId(Long paymentId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<PaymentTransactionJpaEntity> findLockById(Long id);
}
