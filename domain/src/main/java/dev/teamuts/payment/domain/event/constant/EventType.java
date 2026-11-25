package dev.teamuts.payment.domain.event.constant;

import dev.teamuts.payment.domain.event.message.PaymentEventMessage;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.shared.event.BaseEventMessage;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum EventType {
  PAY_ORDER("payment-order-customerpay", PaymentEventMessage.PayCustomerCompleted.class),
  ;

  private final String topic;
  private final Class<? extends BaseEventMessage> messageClass;

  private static final Map<Class<? extends BaseEventMessage>, EventType> BY_MESSAGE_CLASS_MAP =
      Arrays.stream(values())
          .collect(Collectors.toUnmodifiableMap(EventType::getMessageClass, Function.identity()));

  /** All event types related to {@link Payment} */
  private static final EnumSet<EventType> PAYMENT_EVENT_TYPES = EnumSet.of(EventType.PAY_ORDER);

  public static EventType fromMessage(@NonNull BaseEventMessage message) {
    Class<? extends BaseEventMessage> messageClass = message.getClass();

    EventType eventType = BY_MESSAGE_CLASS_MAP.get(messageClass);

    if (eventType == null) {
      throw new IllegalArgumentException(
          "No EventType registered for message class: " + messageClass.getName());
    }

    return eventType;
  }

  public boolean isPaymentEvent() {
    return PAYMENT_EVENT_TYPES.contains(this);
  }
}
