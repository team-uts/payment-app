package dev.teamuts.payment.domain.event.model;

import static org.assertj.core.api.Assertions.assertThat;

import dev.teamuts.payment.domain.event.constant.EventType;
import dev.teamuts.payment.domain.event.constant.OutboxEventStatus;
import dev.teamuts.payment.domain.event.message.PaymentEventMessage;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.factory.PaymentFixtureFactory;
import dev.teamuts.payment.domain.payment.factory.PaymentTransactionFixtureFactory;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.shared.data.AppObjectMapperConfig;
import dev.teamuts.shared.domain.constant.Currency;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;

class OutboxEventTest {
  @Test
  void testOutboxEventCreation() {
    BigDecimal amount = new BigDecimal("100.00");
    Currency currency = Currency.AUD;
    Order order =
        Order.fromDatabase(
            1L,
            "EXT-ORDER-0001",
            AppServiceType.TAXI,
            100L,
            amount,
            currency,
            OrderStatus.CONFIRMED);
    Payment payment =
        PaymentFixtureFactory.createSingle(
            order.getId(), order.getMemberId(), PaymentStatus.COMPLETED, amount, BigDecimal.ZERO);
    PaymentTransaction transaction =
        PaymentTransactionFixtureFactory.createSingle(
            order.getMemberId(),
            payment.getId(),
            order.getId(),
            PaymentMethodType.CARD,
            order.getServiceType(),
            amount,
            currency,
            PgProviderType.TEST,
            PaymentTransactionStatus.COMPLETED,
            PaySequenceType.PG_PAY);
    payment.integrateTransactions(PaymentTransactions.of(List.of(transaction)));

    OutboxEvent result = OutboxEvent.init(PaymentEventMessage.PayCustomerCompleted.of(payment));

    assertThat(result.getEventType()).isEqualTo(EventType.PAY_ORDER);
    assertThat(result.getStatus()).isEqualTo(OutboxEventStatus.INIT);

    JsonNode payload = AppObjectMapperConfig.baseObjectMapper().readTree(result.getPayload());
    assertThat(payload.path("order_id").asLong()).isEqualTo(order.getId());
    assertThat(payload.path("member_id").asLong()).isEqualTo(order.getMemberId());
    assertThat(payload.path("pay_amount").decimalValue()).isEqualByComparingTo(amount);
    assertThat(payload.path("currency").asString()).isEqualTo(currency.name());

    assertThat(payload.path("payment_id").asLong()).isEqualTo(payment.getId());

    JsonNode transactions = payload.path("transaction");
    assertThat(transactions.size()).isEqualTo(1);
    JsonNode transactionPayload = transactions.path(0);
    assertThat(transactionPayload.path("id").asLong()).isEqualTo(transaction.getId());
    assertThat(transactionPayload.path("amount").decimalValue()).isEqualByComparingTo(amount);
    assertThat(transactionPayload.path("pg_provider").asString())
        .isEqualTo(PgProviderType.TEST.name());
    assertThat(transactionPayload.path("pay_method").asString())
        .isEqualTo(PaymentMethodType.CARD.name());
  }
}
