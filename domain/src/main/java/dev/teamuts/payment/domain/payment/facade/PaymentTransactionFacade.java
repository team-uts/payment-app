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
    if (payment.cannotProcessPayTransaction()) {
      throw new RuntimeException("Cannot process payment" + payment.getId());
    }

    // Retrieve or Create multiple PaymentTransaction (should have at least one INIT tx)
    PaymentTransactions transactions =
        paymentTransactionService.getTransactionsOrCreateInit(payment);

    // TODO: In case the subsequent txs are not possible to process, handle the error
    // process transaction list in PaySequenceType sequence
    transactions.processInSequence(paymentTransactionExecutor::processTransaction);

    // update payment status based on the result txs
    paymentService.updateStatus(payment, transactions.resolvePaymentStatus());

    return transactions;
  }
}
