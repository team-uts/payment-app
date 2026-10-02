package dev.teamuts.payment.persistence.payment.repository;

import dev.teamuts.payment.persistence.payment.entity.CardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardJpaEntity, Long> {}
