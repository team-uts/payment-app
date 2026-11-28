package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.event.handler.OutboxEventPublisher;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.error.OrderErrorCode;
import dev.teamuts.payment.domain.order.error.OrderException;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.payment.domain.payment.model.sub.PaymentRequests;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPaymentFacade {
  private final InitPaymentFacade initPaymentFacade;
  private final PaymentProcessor paymentProcessor;
  private final OrderService orderService;
  private final PaymentService paymentService;
  private final OutboxEventPublisher outboxEventPublisher;

  /**
   * Process a payment for the given order.
   *
   * @param order only for Order with {@link OrderStatus#INIT} or {@link
   *     OrderStatus#PAYMENT_PENDING} status
   * @param paymentRequests the payment request details
   * @return result payment
   */
  @AppTransactional(propagation = Propagation.NOT_SUPPORTED)
  public Payments processPayOrder(Order order, PaymentRequests paymentRequests) {
    if (order.cannotProcessPayment()) {
      throw OrderException.of(
          OrderErrorCode.CANNOT_PAY_PROCESS, order.getExtOrderNum(), order.getStatus());
    }

    // Retrieve or Create Payment (PROCESSING)
    Payments payments = initPaymentFacade.getProcessingPaymentsOrCreate(order, paymentRequests);

    // Prepare Order for Payment process
    orderService.prepareForPayment(order);

    // Process each payment
    payments.processEachPayment(paymentProcessor::processPayment);

    return payments;
  }

  /**
   * publish the completed pay order event for the given order.<br>
   * When they have any exceptions here, just log the error to notify through other channels<br>
   * (That would be Slack channels using Sentry for our projects)
   */
  @AppTransactional
  public void publishCompletedPayOrderEvent(Order order) {
    try {
      if (order.cannotPublishPayEvent()) {
        return;
      }

      // if the order needs to get payments, retrieve them and insert into order
      if (order.hasNoPayments()) {
        Payments payments = paymentService.getPaymentsWithOutboxEvent(order);
        order.insertPaymentsIfEmpty(payments, true);
      }

      // publish events for all completed orders
      order.processAllPaymentOutboxEvents(outboxEventPublisher::publish);
    } catch (Exception e) {
      log.error(
          "Failed to publish completed pay order event for order: {}, error: {}",
          order.getExtOrderNum(),
          e.getMessage(),
          e);
    }
  }
}
