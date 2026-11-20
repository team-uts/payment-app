package dev.teamuts.payment.infra.event.adapter;

import dev.teamuts.payment.domain.event.port.infra.EventMessageProducePort;
import dev.teamuts.shared.domain.annotation.InfraAdapter;
import dev.teamuts.shared.event.BaseEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@InfraAdapter
@RequiredArgsConstructor
public class KafkaEventMessageProducePort implements EventMessageProducePort {
  private final KafkaTemplate<String, BaseEventMessage> kafkaTemplate;

  @Override
  public void produce(BaseEventMessage eventMessage) {}
}
