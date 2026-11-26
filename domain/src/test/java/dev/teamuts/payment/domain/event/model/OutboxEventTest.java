package dev.teamuts.payment.domain.event.model;

import static org.assertj.core.api.Assertions.assertThat;

import dev.teamuts.payment.domain.event.constant.EventType;
import dev.teamuts.payment.domain.event.constant.OutboxEventStatus;
import dev.teamuts.payment.domain.event.message.PaymentEventMessage;
import dev.teamuts.payment.domain.order.OrderFixtureFactory;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.factory.PaymentFixtureFactory;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.shared.data.AppObjectMapperConfig;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;

class OutboxEventTest {
  @Test
  void testOutboxEventCreation() {
    BigDecimal payAmount = new BigDecimal("100.00");
    BigDecimal pointAmount = new BigDecimal("20.00");
    Order order = OrderFixtureFactory.createSingle(OrderStatus.CONFIRMED);
    Payment payment =
        PaymentFixtureFactory.createSingle(
            order.getId(), order.getMemberId(), PaymentStatus.COMPLETED, payAmount, pointAmount);

    OutboxEvent result = OutboxEvent.init(PaymentEventMessage.of(order, payment));

    assertThat(result.getEventType()).isEqualTo(EventType.PAY_ORDER);
    assertThat(result.getStatus()).isEqualTo(OutboxEventStatus.INIT);

    JsonNode payload = AppObjectMapperConfig.baseObjectMapper().readTree(result.getPayload());
    assertThat(payload.path("payment_id").asLong()).isEqualTo(payment.getId());
    assertThat(payload.path("order_id").asLong()).isEqualTo(order.getId());
    assertThat(payload.path("ext_order_num").asString()).isEqualTo(order.getExtOrderNum());
    assertThat(payload.path("member_id").asLong()).isEqualTo(order.getMemberId());
    assertThat(payload.path("service_type").asString()).isEqualTo(order.getServiceType().name());
    assertThat(payload.path("payment_type").asString()).isEqualTo(payment.getPaymentType().name());
    assertThat(payload.path("status").asString()).isEqualTo(PaymentStatus.COMPLETED.name());
    assertThat(payload.path("pay_amount").decimalValue()).isEqualByComparingTo(payAmount);
    assertThat(payload.path("point_amount").decimalValue()).isEqualByComparingTo(pointAmount);
    assertThat(payload.path("currency").asString()).isEqualTo(payment.getCurrency().name());
    assertThat(payload.path("pay_method_id").asLong()).isEqualTo(payment.getPayMethodId());
    assertThat(payload.path("pay_method_type").asString())
        .isEqualTo(payment.getPayMethodType().name());
    assertThat(payload.path("pg_provider").asString()).isEqualTo(payment.getPgProvider().name());
    assertThat(payload.has("transaction")).isFalse();
  }
}
