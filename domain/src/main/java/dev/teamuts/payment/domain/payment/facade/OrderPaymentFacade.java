package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.error.OrderErrorCode;
import dev.teamuts.payment.domain.order.error.OrderException;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.error.PaymentErrorCode;
import dev.teamuts.payment.domain.payment.error.PaymentException;
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
   * @param paymentType the type of payment to be processed
   * @return result payment
   */
  public Payment processPayment(Order order, PayOrderCommand command, PaymentType paymentType) {
    if (order.cannotProcessPayment()) {
      throw OrderException.of(
          OrderErrorCode.CANNOT_PAY_PROCESS, order.getExtOrderNum(), order.getStatus());
    }

    // Retrieve or Create Payment (PROCESSING)
    Payment payment = initPaymentFacade.getProcessingPaymentOrCreate(order, command, paymentType);

    // Check if the payment can be processed
    if (payment.cannotProcessPayTransaction()) {
      throw PaymentException.of(PaymentErrorCode.CANNOT_PROCESS_PAY_TX, payment.getId());
    }

    // Prepare Order for Payment process
    orderService.prepareForPayment(order);

    // Retrieve or Create multiple PaymentTransaction (should have at least one INIT tx)
    PaymentTransactions transactions =
        paymentTransactionService.getTransactionsOrCreateInit(payment);

    // **IMPORTANT** process transaction list in PaySequenceType sequence
    transactions.processInSequence(paymentTransactionExecutor::executeProcessing);

    // update payment status based on the result txs
    paymentService.integrateTransactions(payment, transactions);

    return payment;
  }
}
