package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.order.constant.OrderStatus;
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

  /**
   * Process a payment for the given order.
   *
   * @param order only for Order with {@link OrderStatus#INIT} or {@link
   *     OrderStatus#PAYMENT_PENDING} status
   * @param command request data for payment processing
   * @return result payment
   */
  public Payment processPayment(Order order, PayOrderCommand command) {
    if (order.cannotProcessPayment()) {
      throw new RuntimeException(
          "Cannot process payment for order (extOrderNum: %s) with status %s"
              .formatted(order.getExtOrderNum(), order.getStatus()));
    }

    // Retrieve or Create Payment (PROCESSING)
    Payment payment = initPaymentFacade.getProcessingPaymentOrCreate(order, command);

    // Check if the payment can be processed
    if (payment.cannotProcessPayTransaction()) {
      throw new RuntimeException("Cannot process payment" + payment.getId());
    }

    // Prepare Order for Payment process
    orderService.prepareForPayment(order);

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
