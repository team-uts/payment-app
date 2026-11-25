package dev.teamuts.payment.domain.event.handler;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
public class OutboxEventPublisher {
  private final ApplicationEventPublisher eventPublisher;

  /** require transactional context. {@link Propagation#MANDATORY} */
  @AppTransactional(propagation = Propagation.MANDATORY)
  public void publish(OutboxEvent event) {
    event.checkPersisted();

    eventPublisher.publishEvent(event);
  }
}
