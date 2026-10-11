package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentTransactionFacade {
  private final PaymentTransactionExecutor paymentTransactionExecutor;
  private final PaymentTransactionService paymentTransactionService;
  private final PaymentService paymentService;

  public PaymentTransactions processPayment(Payment payment) {
    if (payment.isNotProcessing()) {
      throw new RuntimeException("Not processing payment" + payment.getId());
    }

    // Retrieve or Create multiple PaymentTransaction (should have at least one INIT tx)
    PaymentTransactions transactions =
        paymentTransactionService.getTransactionsOrCreateInit(payment);

    // process transaction list in PaySequenceType sequence
    PaymentTransactions resultTransactions =
        transactions.processInSequence(paymentTransactionExecutor::processTransaction);

    // update payment status based on the result txs
    paymentService.updateStatus(payment, resultTransactions.resolvePaymentStatus());

    return resultTransactions;
  }
}
