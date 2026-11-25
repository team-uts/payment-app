package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentUpdatePort;
import dev.teamuts.payment.persistence.event.entity.OutboxEventJpaEntity;
import dev.teamuts.payment.persistence.event.repository.OutboxEventRepository;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import dev.teamuts.payment.persistence.payment.repository.PaymentRepository;
import dev.teamuts.shared.data.PersistenceException;
import dev.teamuts.shared.data.annotation.AppTransactional;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class PaymentUpdateAdapter implements PaymentUpdatePort {
  private final PaymentRepository paymentRepository;
  private final OutboxEventRepository outboxEventRepository;

  @Override
  public void updateStatus(Payment payment) {
    PaymentJpaEntity entity =
        paymentRepository
            .findLockById(payment.getId())
            .orElseThrow(() -> PersistenceException.notFound(payment));

    entity.updateStatus(payment.getStatus());
  }

  @Override
  public void updateOutboxEvent(Payment payment) {
    PaymentJpaEntity entity =
        paymentRepository
            .findLockById(payment.getId())
            .orElseThrow(() -> PersistenceException.notFound(payment));

    OutboxEventJpaEntity outboxEventEntity =
        outboxEventRepository
            .findById(payment.getOutboxEventId())
            .orElseThrow(
                () -> PersistenceException.notFound(OutboxEvent.class, payment.getOutboxEventId()));

    entity.updateOutboxEvent(outboxEventEntity);
  }
}
