package dev.teamuts.payment.domain.order.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;

@UseCase
public class PayOrderUseCase {
  public void execute(PayOrderCommand command) {
    // Create new Order data (INIT)

    // Create new Payment (INIT)

    // Create new PaymentTransaction data (INIT)

    // Request payment to Payment Gateway (PG)

    // Update PaymentTransaction data with PG response

    // Update Payment data to "PAID"

    // Update Order status to "CONFIRMED"
  }
}
