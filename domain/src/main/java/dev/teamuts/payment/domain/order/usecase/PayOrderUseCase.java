package dev.teamuts.payment.domain.order.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.facade.PaymentFacade;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.payment.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PayOrderUseCase {
  private final OrderService orderService;
  private final PaymentFacade paymentFacade;
  private final PaymentMethodService paymentMethodService;
  private final PaymentService paymentService;
  private final PointService pointService;

  // TODO: require Redis Locking
  public void execute(Long memberId, PayOrderCommand command) {
    // Retrieve or Create new Order data (PAYMENT_PENDING)
    Order order = orderService.getInitPaymentOrderOrCreate(memberId, command);

    // Retrieve or Create Payment (PROCESSING)
    Payment payment = paymentFacade.getProcessingPaymentOrCreate(order, command);

    // Create new PaymentTransaction data (INIT)

    // Request payment to Payment Gateway (PG)

    // Update PaymentTransaction data with PG response

    // Update Payment data to "PAID"

    // Update Order status to "CONFIRMED"
  }
}
