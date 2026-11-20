package dev.teamuts.payment.persistence.event.mapper;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.persistence.event.entity.OutboxEventJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventConverter {
  public OutboxEventJpaEntity convertToJpaEntity(OutboxEvent outboxEvent) {
    return OutboxEventJpaEntity.newEntity(outboxEvent);
  }

  public OutboxEvent convertToDomainModel(OutboxEventJpaEntity outboxEventJpaEntity) {
    return OutboxEvent.fromDatabase(
        outboxEventJpaEntity.getId(),
        outboxEventJpaEntity.getEventType(),
        outboxEventJpaEntity.getPayload(),
        outboxEventJpaEntity.getStatus());
  }
}
