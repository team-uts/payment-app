package dev.teamuts.payment.domain.event.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dev.teamuts.payment.domain.event.constant.EventType;
import dev.teamuts.payment.domain.event.constant.OutboxEventStatus;
import dev.teamuts.payment.domain.event.message.OrderEventMessage;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.factory.PaymentTransactionFixtureFactory;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.shared.domain.constant.Currency;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutboxEventTest {
  private OrderEventMessage.PayCustomerOrder givenMessage;

  @BeforeEach
  void setup() {
    givenMessage =
        OrderEventMessage.PayCustomerOrder.of(
            PaymentTransactionFixtureFactory.createSingle(
                1L,
                100L,
                1000L,
                PaymentMethodType.CARD,
                AppServiceType.TAXI,
                new BigDecimal("100.00"),
                Currency.AUD,
                PgProviderType.TEST,
                PaymentTransactionStatus.READY,
                PaySequenceType.PG_PAY));
  }

  @Test
  void testOutboxEventCreation() {
    OutboxEvent result = OutboxEvent.init(givenMessage);
    assertThat(result.getEventType()).isEqualTo(EventType.PAY_ORDER);
    assertThat(result.getStatus()).isEqualTo(OutboxEventStatus.INIT);
    assertThat(result.getPayload())
        .contains(
            "\"currency\":\"AUD\"",
            "\"payment_type\":\"TAXI_FARE\"",
            "\"pay_transaction_id\":" + givenMessage.payTransactionId());
  }

  @Test
  void testOutboxEventPayloadParsing() {
    OutboxEvent outboxEvent = OutboxEvent.init(givenMessage);
    OrderEventMessage.PayCustomerOrder parsedMessage =
        (OrderEventMessage.PayCustomerOrder) outboxEvent.getEventMessage();

    assertThat(parsedMessage.payAmount()).isEqualTo(givenMessage.payAmount());
    assertThat(parsedMessage.currency()).isEqualTo(givenMessage.currency());
    assertThat(parsedMessage.payTransactionId()).isEqualTo(givenMessage.payTransactionId());
    assertThat(parsedMessage.paymentType()).isEqualTo(givenMessage.paymentType());
  }
}
