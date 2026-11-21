package dev.teamuts.payment.domain.event.constant;

import dev.teamuts.payment.domain.event.message.OrderEventMessage;
import dev.teamuts.shared.event.EventMessagePublishable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum EventType {
  PAY_ORDER("payment-order-customerpay", OrderEventMessage.PayCustomerOrder.class),
  ;

  private final String topic;
  private final Class<? extends EventMessagePublishable> messageClass;

  private static final Map<Class<? extends EventMessagePublishable>, EventType>
      BY_MESSAGE_CLASS_MAP =
          Arrays.stream(values())
              .collect(
                  Collectors.toUnmodifiableMap(EventType::getMessageClass, Function.identity()));

  public static EventType fromMessage(@NonNull EventMessagePublishable message) {
    Class<? extends EventMessagePublishable> messageClass = message.getClass();

    EventType eventType = BY_MESSAGE_CLASS_MAP.get(messageClass);

    if (eventType == null) {
      throw new IllegalArgumentException(
          "No EventType registered for message class: " + messageClass.getName());
    }

    return eventType;
  }
}
