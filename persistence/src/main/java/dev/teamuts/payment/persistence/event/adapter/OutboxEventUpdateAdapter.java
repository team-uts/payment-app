package dev.teamuts.payment.persistence.event.adapter;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventUpdatePort;
import dev.teamuts.payment.persistence.event.entity.OutboxEventJpaEntity;
import dev.teamuts.payment.persistence.event.repository.OutboxEventRepository;
import dev.teamuts.shared.data.PersistenceException;
import dev.teamuts.shared.data.annotation.AppTransactional;
import dev.teamuts.shared.data.annotation.RequireEntityLock;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class OutboxEventUpdateAdapter implements OutboxEventUpdatePort {
  private final OutboxEventRepository outboxEventRepository;

  @RequireEntityLock(entity = OutboxEventJpaEntity.class)
  @Override
  public void updateStatus(OutboxEvent outboxEvent) {
    OutboxEventJpaEntity entity =
        outboxEventRepository
            .findById(outboxEvent.getId())
            .orElseThrow(() -> PersistenceException.notFound(outboxEvent));

    entity.updateStatus(outboxEvent.getStatus());
  }
}
