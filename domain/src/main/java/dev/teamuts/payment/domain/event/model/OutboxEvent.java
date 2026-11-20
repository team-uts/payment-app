package dev.teamuts.payment.domain.event.model;

import dev.teamuts.payment.domain.event.constant.EventType;
import dev.teamuts.payment.domain.event.constant.OutboxEventStatus;
import dev.teamuts.shared.data.AppObjectMapperConfig;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import dev.teamuts.shared.event.BaseEventMessage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@DomainModel
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class OutboxEvent extends BaseIdentifiableModel<Long> {
  private EventType eventType;
  private String payload;
  private OutboxEventStatus status;

  public static final int LIMITED_PAYLOAD_LENGTH = 1000;

  private static final ObjectMapper objectMapper = AppObjectMapperConfig.baseObjectMapper();

  public static OutboxEvent init(BaseEventMessage message) {
    JsonNode jsonNode = objectMapper.convertValue(message, JsonNode.class);

    return OutboxEvent.builder()
        .eventType(EventType.fromMessage(message))
        .payload(jsonNode.toString())
        .status(OutboxEventStatus.INIT)
        .build();
  }

  public static OutboxEvent fromDatabase(
      Long id, EventType eventType, String payload, OutboxEventStatus status) {
    OutboxEvent outboxEvent =
        OutboxEvent.builder().eventType(eventType).payload(payload).status(status).build();

    outboxEvent.setId(id);

    return outboxEvent;
  }
}
