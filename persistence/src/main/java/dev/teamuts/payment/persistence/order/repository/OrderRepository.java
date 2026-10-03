package dev.teamuts.payment.persistence.order.repository;

import dev.teamuts.payment.persistence.order.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderJpaEntity, Long> {}
