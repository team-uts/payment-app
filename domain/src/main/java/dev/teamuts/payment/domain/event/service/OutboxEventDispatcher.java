package dev.teamuts.payment.domain.event.service;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OutboxEventDispatcher {
  private final OutboxEventService outboxEventService;

  /**
   * Use asynchronous worker thread to respond to end user requests<br>
   * not waiting for event publication.
   */
  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void dispatch(OutboxEvent outboxEvent) {
    outboxEventService.send(outboxEvent);
  }
}
