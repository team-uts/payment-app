package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.persistence.payment.entity.PaymentTransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository
    extends JpaRepository<PaymentTransactionJpaEntity, Long> {}
