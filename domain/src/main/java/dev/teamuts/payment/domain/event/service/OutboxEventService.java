package dev.teamuts.payment.domain.event.service;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.port.infra.EventMessageProducePort;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
public class OutboxEventService {
  private final EventMessageProducePort eventMessageProducePort;

  @AppTransactional(propagation = Propagation.REQUIRES_NEW)
  public void send(OutboxEvent outboxEvent) {}
}
