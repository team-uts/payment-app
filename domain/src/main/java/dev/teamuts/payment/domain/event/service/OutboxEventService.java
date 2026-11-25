package dev.teamuts.payment.domain.event.service;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.port.infra.EventMessageProducePort;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventReaderPort;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventStorePort;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventUpdatePort;
import dev.teamuts.shared.data.annotation.AppTransactional;
import dev.teamuts.shared.event.BaseEventMessage;
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
  private final OutboxEventStorePort outboxEventStorePort;

  @AppTransactional(propagation = Propagation.REQUIRES_NEW)
  public void send(OutboxEvent outboxEvent) {
    OutboxEvent lockedEvent = outboxEventReaderPort.retrieveByIdWithLock(outboxEvent.getId());

    // do not send the event which is already processed
    if (lockedEvent.isProcessed()) {
      log.info("Outbox event is already processed: {}", lockedEvent.getId());
      return;
    }

    try {
      eventMessageProducePort.produce(lockedEvent);
      lockedEvent.processed();
    } catch (Exception e) {
      log.error("Failed to send outbox event: {}", lockedEvent.getId(), e);
      lockedEvent.failed();
    }

    outboxEventUpdatePort.updateStatus(lockedEvent);
  }

  @AppTransactional
  public OutboxEvent createInitEvent(BaseEventMessage eventMessage) {
    OutboxEvent outboxEvent = OutboxEvent.init(eventMessage);

    return outboxEventStorePort.store(outboxEvent);
  }
}
