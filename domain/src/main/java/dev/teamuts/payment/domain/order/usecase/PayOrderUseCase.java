package dev.teamuts.payment.domain.order.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PayOrderUseCase {
  private final OrderService orderService;

  public void execute(Long memberId, PayOrderCommand command) {
    // Create new Order data (INIT)
    Order order = orderService.initializeOrder(memberId, command);

    // Create new Payment (INIT)

    // Create new PaymentTransaction data (INIT)

    // Request payment to Payment Gateway (PG)

    // Update PaymentTransaction data with PG response

    // Update Payment data to "PAID"

    // Update Order status to "CONFIRMED"
  }
}
