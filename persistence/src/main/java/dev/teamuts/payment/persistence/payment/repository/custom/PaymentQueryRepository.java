package dev.teamuts.payment.persistence.payment.repository.custom;

import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import java.util.List;

public interface PaymentQueryRepository {
  List<PaymentJpaEntity> findAllByOrderIdJoiningTransactions(Long orderId);
}
