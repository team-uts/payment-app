package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentJpaEntity, Long> {}
