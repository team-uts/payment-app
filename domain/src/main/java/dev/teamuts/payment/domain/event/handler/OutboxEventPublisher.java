package dev.teamuts.payment.domain.event.handler;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.port.persistence.OutboxEventStorePort;
import dev.teamuts.shared.data.annotation.AppTransactional;
import dev.teamuts.shared.event.EventMessagePublishable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
public class OutboxEventPublisher {
  private final ApplicationEventPublisher eventPublisher;
  private final OutboxEventStorePort outboxEventStorePort;

  /** require transactional context. {@link Propagation#MANDATORY} */
  @AppTransactional(propagation = Propagation.MANDATORY)
  public OutboxEvent publish(EventMessagePublishable eventMessage) {
    OutboxEvent outboxEvent = OutboxEvent.init(eventMessage);

    OutboxEvent storedEvent = outboxEventStorePort.store(outboxEvent);

    eventPublisher.publishEvent(storedEvent);

    return storedEvent;
  }
}
