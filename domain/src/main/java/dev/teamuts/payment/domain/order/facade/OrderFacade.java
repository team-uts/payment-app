package dev.teamuts.payment.domain.order.facade;

import dev.teamuts.payment.domain.event.message.PaymentEventMessage;
import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.event.service.OutboxEventService;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacade {
  private final OrderService orderService;
  private final PaymentService paymentService;
  private final OutboxEventService outboxEventService;

  public void finalizePayOrder(Order order, Payments payments) {
    // apply the payment results to the order and update its status
    orderService.applyPaymentResults(order, payments);

    if (order.cannotPublishPayEvent()) {
      return;
    }

    // ready for publishing outbox events for each payment
    payments.forEach(payment -> {
      // create init outbox event
      OutboxEvent storedEvent =
          outboxEventService.createInitEvent(PaymentEventMessage.PayCustomerCompleted.of(payment));

      // link outbox event to payment
      paymentService.linkOutboxEvent(payment, storedEvent);
    });
  }
}
