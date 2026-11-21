package dev.teamuts.payment.persistence.event.adapter;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventReaderPort;
import dev.teamuts.payment.persistence.event.entity.OutboxEventJpaEntity;
import dev.teamuts.payment.persistence.event.mapper.OutboxEventConverter;
import dev.teamuts.payment.persistence.event.repository.OutboxEventRepository;
import dev.teamuts.shared.data.PersistenceException;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class OutboxEventReaderAdapter implements OutboxEventReaderPort {
  private final OutboxEventRepository outboxEventRepository;
  private final OutboxEventConverter outboxEventConverter;

  @Override
  public OutboxEvent retrieveByIdWithLock(Long id) {
    OutboxEventJpaEntity entity =
        outboxEventRepository
            .findLockById(id)
            .orElseThrow(() -> PersistenceException.notFound(OutboxEvent.class, id));

    return outboxEventConverter.convertToDomainModel(entity);
  }
}
