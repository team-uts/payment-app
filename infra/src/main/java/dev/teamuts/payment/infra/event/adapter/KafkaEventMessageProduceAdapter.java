package dev.teamuts.payment.infra.event.adapter;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.port.infra.EventMessageProducePort;
import dev.teamuts.shared.domain.annotation.InfraAdapter;
import dev.teamuts.shared.event.EventMessagePublishable;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@InfraAdapter
@RequiredArgsConstructor
public class KafkaEventMessageProduceAdapter implements EventMessageProducePort {
  private final KafkaTemplate<String, EventMessagePublishable> kafkaTemplate;

  @Override
  public void produce(OutboxEvent outboxEvent) {
    kafkaTemplate.send(outboxEvent.getEventType().getTopic(), outboxEvent.getEventMessage()).join();
  }
}
