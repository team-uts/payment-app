package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPaymentFacade {
  private final InitPaymentFacade initPaymentFacade;
  private final PaymentTransactionExecutor paymentTransactionExecutor;
  private final OrderService orderService;
  private final PaymentTransactionService paymentTransactionService;
  private final PaymentService paymentService;

  public Payment processPayment(Order order, PayOrderCommand command) {
    // Retrieve or Create Payment (PROCESSING)
    Payment payment = initPaymentFacade.getProcessingPaymentOrCreate(order, command);

    // Prepare Order for Payment process
    orderService.prepareForPayment(order);

    if (payment.cannotProcessPayTransaction()) {
      throw new RuntimeException("Cannot process payment" + payment.getId());
    }

    // Retrieve or Create multiple PaymentTransaction (should have at least one INIT tx)
    PaymentTransactions transactions =
        paymentTransactionService.getTransactionsOrCreateInit(payment);

    // process transaction list in PaySequenceType sequence
    transactions.processInSequence(paymentTransactionExecutor::executeProcessing);

    // update payment status based on the result txs
    paymentService.integrateTransactions(payment, transactions);

    return payment;
  }
}
