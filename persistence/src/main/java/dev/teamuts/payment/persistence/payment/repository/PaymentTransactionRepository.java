package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.persistence.payment.entity.PaymentTransactionJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository
    extends JpaRepository<PaymentTransactionJpaEntity, Long> {
  List<PaymentTransactionJpaEntity> findAllByPaymentId(Long paymentId);
}
