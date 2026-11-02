package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.common.exception.PersistenceException;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentUpdatePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import dev.teamuts.payment.persistence.payment.repository.PaymentRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class PaymentUpdateAdapter implements PaymentUpdatePort {
  private final PaymentRepository paymentRepository;

  @Override
  public void updateStatus(Payment payment) {
    PaymentJpaEntity entity =
        paymentRepository
            .findLockById(payment.getId())
            .orElseThrow(() -> PersistenceException.notFound(payment));

    entity.updateStatus(payment.getStatus());
  }
}
