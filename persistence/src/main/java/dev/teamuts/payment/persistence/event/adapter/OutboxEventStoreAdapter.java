package dev.teamuts.payment.persistence.event.adapter;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventStorePort;
import dev.teamuts.payment.persistence.event.entity.OutboxEventJpaEntity;
import dev.teamuts.payment.persistence.event.mapper.OutboxEventConverter;
import dev.teamuts.payment.persistence.event.repository.OutboxEventRepository;
import dev.teamuts.shared.data.annotation.AppTransactional;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class OutboxEventStoreAdapter implements OutboxEventStorePort {
  private final OutboxEventRepository outboxEventRepository;
  private final OutboxEventConverter outboxEventConverter;

  @Override
  public OutboxEvent store(OutboxEvent event) {
    OutboxEventJpaEntity entity = outboxEventConverter.convertToJpaEntity(event);

    OutboxEventJpaEntity savedEntity = outboxEventRepository.save(entity);

    return outboxEventConverter.convertToDomainModel(savedEntity);
  }
}
