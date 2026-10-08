package dev.teamuts.payment.domain.order.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.facade.PaymentFacade;
import dev.teamuts.payment.domain.payment.facade.pay.PayTransactionProcessorProvider;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PayOrderUseCase {
  private final OrderService orderService;
  private final PaymentFacade paymentFacade;
  private final PaymentTransactionService paymentTransactionService;
  private final PayTransactionProcessorProvider payTxProcessorProvider;

  // TODO: require Redis Locking
  public void execute(Long memberId, PayOrderCommand command) {
    // Retrieve or Create new Order data (PAYMENT_PENDING)
    Order order = orderService.getInitPaymentOrderOrCreate(memberId, command);

    // Retrieve or Create Payment (PROCESSING)
    Payment payment = paymentFacade.getProcessingPaymentOrCreate(order, command);

    // Create new PaymentTransactions data (INIT)
    PaymentTransactions transactions = paymentTransactionService.createInitTransactions(payment);

    // Request payment to Payment Gateway (PG)
    transactions.processInSequence(
        transaction -> {
          PaymentTransaction resultTx =
              payTxProcessorProvider.getInstance(transaction.getSequenceType()).pay(transaction);

          return resultTx;
        });

    // Update PaymentTransaction data with PG response

    // Update Payment data to "PAID"

    // Update Order status to "CONFIRMED"
  }
}
