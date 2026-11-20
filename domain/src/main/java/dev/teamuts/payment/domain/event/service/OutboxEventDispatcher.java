package dev.teamuts.payment.domain.event.service;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OutboxEventDispatcher {
  private final OutboxEventService outboxEventService;

  //  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void dispatch(OutboxEvent outboxEvent) {
    outboxEventService.send(outboxEvent);
  }
}
