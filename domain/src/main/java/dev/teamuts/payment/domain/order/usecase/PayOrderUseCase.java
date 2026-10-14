package dev.teamuts.payment.domain.order.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.facade.PaymentFacade;
import dev.teamuts.payment.domain.payment.facade.PaymentTransactionFacade;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PayOrderUseCase {
  private final OrderService orderService;
  private final PaymentFacade paymentFacade;
  private final PaymentTransactionFacade paymentTransactionFacade;

  // TODO: require Redis Locking
  public void execute(Long memberId, PayOrderCommand command) {
    // Retrieve or Create new Order data (PAYMENT_PENDING)
    Order order = orderService.getInitPaymentOrderOrCreate(memberId, command);

    // Retrieve or Create Payment (PROCESSING)
    Payment payment = paymentFacade.getProcessingPaymentOrCreate(order, command);

    // Prepare Order for Payment process
    orderService.prepareForPayment(order);

    // Create new PaymentTransactions data (INIT)
    PaymentTransactions transactions = paymentTransactionFacade.processPayment(payment);

    // Update PaymentTransaction data with PG response

    // Update Payment data to "PAID"

    // Update Order status to "CONFIRMED"
  }
}
