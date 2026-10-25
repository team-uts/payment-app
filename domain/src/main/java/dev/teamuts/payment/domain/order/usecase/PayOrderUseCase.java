package dev.teamuts.payment.domain.order.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.dto.PayOrderInfo;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.facade.InitPaymentFacade;
import dev.teamuts.payment.domain.payment.facade.OrderPaymentFacade;
import dev.teamuts.payment.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PayOrderUseCase {
  private final OrderService orderService;
  private final InitPaymentFacade paymentFacade;
  private final OrderPaymentFacade paymentTransactionFacade;

  // TODO: require Redis Locking
  public PayOrderInfo execute(Long memberId, PayOrderCommand command) {
    // Retrieve or Create new Order data (PAYMENT_PENDING)
    Order order = orderService.getPaymentOrderOrCreateInit(memberId, command);

    // Process Payment with Order
    Payment resultPayment = paymentTransactionFacade.processPayment(order, command);

    // Update Order status to "CONFIRMED"
    orderService.updateStatusWithPayment(order, resultPayment);

    return PayOrderInfo.of(order);
  }
}
