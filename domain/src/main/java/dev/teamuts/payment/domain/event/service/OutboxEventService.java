package dev.teamuts.payment.domain.event.service;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.port.infra.EventMessageProducePort;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventReaderPort;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventUpdatePort;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventService {
  private final EventMessageProducePort eventMessageProducePort;
  private final OutboxEventReaderPort outboxEventReaderPort;
  private final OutboxEventUpdatePort outboxEventUpdatePort;

  @AppTransactional(propagation = Propagation.REQUIRES_NEW)
  public void send(OutboxEvent outboxEvent) {
    OutboxEvent lockedEvent = outboxEventReaderPort.retrieveByIdWithLock(outboxEvent.getId());

    try {
      eventMessageProducePort.produce(lockedEvent);
      lockedEvent.processed();
    } catch (Exception e) {
      log.error("Failed to send outbox event: {}", lockedEvent.getId(), e);
      lockedEvent.failed();
    }

    outboxEventUpdatePort.updateStatus(lockedEvent);
  }
}
