package dev.teamuts.payment.domain.order.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.dto.PayOrderInfo;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.facade.PaymentFacade;
import dev.teamuts.payment.domain.payment.facade.PaymentTransactionFacade;
import dev.teamuts.payment.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PayOrderUseCase {
  private final OrderService orderService;
  private final PaymentFacade paymentFacade;
  private final PaymentTransactionFacade paymentTransactionFacade;

  // TODO: require Redis Locking
  public PayOrderInfo execute(Long memberId, PayOrderCommand command) {
    // Retrieve or Create new Order data (PAYMENT_PENDING)
    Order order = orderService.getInitPaymentOrderOrCreate(memberId, command);

    // Retrieve or Create Payment (PROCESSING)
    Payment payment = paymentFacade.getProcessingPaymentOrCreate(order, command);

    // Prepare Order for Payment process
    orderService.prepareForPayment(order);

    // Create new PaymentTransactions data (INIT)
    Payment resultPayment = paymentTransactionFacade.processPayment(payment);

    // Update Order status to "CONFIRMED"
    orderService.updateStatusWithPayment(order, resultPayment);

    return PayOrderInfo.of(order);
  }
}
