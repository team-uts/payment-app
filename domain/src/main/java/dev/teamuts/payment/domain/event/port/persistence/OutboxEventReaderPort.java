package dev.teamuts.payment.domain.event.port.persistence;

import dev.teamuts.payment.domain.event.model.OutboxEvent;

public interface OutboxEventReaderPort {
  OutboxEvent retrieveByIdWithLock(Long id);
}
