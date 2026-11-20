package dev.teamuts.payment.domain.event.port.infra;

import dev.teamuts.shared.event.BaseEventMessage;

public interface EventMessageProducePort {
  void produce(BaseEventMessage eventMessage);
}
