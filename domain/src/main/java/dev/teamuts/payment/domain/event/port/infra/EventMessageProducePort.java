package dev.teamuts.payment.domain.event.port.infra;

import dev.teamuts.payment.domain.event.model.OutboxEvent;

public interface EventMessageProducePort {
  void produce(OutboxEvent outboxEvent);
}
