package dev.teamuts.payment.domain.event.model;

import dev.teamuts.payment.domain.event.constant.EventType;
import dev.teamuts.payment.domain.event.constant.OutboxEventStatus;
import dev.teamuts.payment.domain.event.error.OutboxEventErrorCode;
import dev.teamuts.shared.data.AppObjectMapperConfig;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import dev.teamuts.shared.error.exception.SystemException;
import dev.teamuts.shared.event.BaseEventMessage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
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

  public BaseEventMessage getEventMessage() {
    try {
      BaseEventMessage baseEventMessage =
          objectMapper.readValue(payload, eventType.getMessageClass());
      // inject the outbox event id into its event message.
      // this is for the idempotency in kafka consumer side.
      baseEventMessage.setIdentifier(getId());
      return baseEventMessage;
    } catch (Exception e) {
      log.error("Failed to parse payload to event message", e);
      throw SystemException.of(OutboxEventErrorCode.PAYLOAD_PARSING_ERROR);
    }
  }

  public void failed() {
    checkPersisted();

    this.status = OutboxEventStatus.FAILED;
  }

  public void processed() {
    checkPersisted();

    this.status = OutboxEventStatus.PROCESSED;
  }

  public boolean isProcessed() {
    return this.status == OutboxEventStatus.PROCESSED;
  }

  public boolean isNotPaymentEvent() {
    return !eventType.isPaymentEvent();
  }

  public OutboxEvent copy() {
    OutboxEvent copy = this.toBuilder().build();
    copy.setId(this.getId());

    return copy;
  }
}
