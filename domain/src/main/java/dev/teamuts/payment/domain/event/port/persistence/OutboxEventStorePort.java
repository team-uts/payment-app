package dev.teamuts.payment.domain.event.port.persistence;

import dev.teamuts.payment.domain.event.model.OutboxEvent;

public interface OutboxEventStorePort {
  OutboxEvent store(OutboxEvent event);
}
