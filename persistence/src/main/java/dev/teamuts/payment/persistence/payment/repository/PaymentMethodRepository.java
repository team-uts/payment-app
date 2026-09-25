package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.persistence.payment.entity.PaymentMethodJpaEntity;
import dev.teamuts.payment.persistence.payment.repository.custom.PaymentMethodQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository
    extends JpaRepository<PaymentMethodJpaEntity, Long>, PaymentMethodQueryRepository {}
