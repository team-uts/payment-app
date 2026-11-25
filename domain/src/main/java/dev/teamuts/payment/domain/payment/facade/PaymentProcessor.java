package dev.teamuts.payment.domain.payment.facade;

import static org.springframework.transaction.annotation.Propagation.*;

import dev.teamuts.payment.domain.payment.error.PaymentErrorCode;
import dev.teamuts.payment.domain.payment.error.PaymentException;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessor {
  private final PaymentTransactionExecutor paymentTransactionExecutor;
  private final PaymentTransactionService paymentTransactionService;
  private final PaymentService paymentService;

  @AppTransactional(propagation = NOT_SUPPORTED)
  public void processPayment(Payment payment) {
    // if the payment was already finalized, just pass
    if (payment.isFinalized()) {
      return;
    }

    // Check if the payment can be processed
    if (payment.cannotProcessPayTransaction()) {
      throw PaymentException.of(PaymentErrorCode.CANNOT_PROCESS_PAY_TX, payment.getId());
    }

    // Retrieve or Create multiple PaymentTransaction (should have at least one INIT tx)
    PaymentTransactions transactions =
        paymentTransactionService.getTransactionsOrCreateInit(payment);

    // **IMPORTANT** process transaction list in PaySequenceType sequence
    transactions.processInSequence(paymentTransactionExecutor::executeProcessing);

    // update payment status based on the result txs
    paymentService.integrateTransactions(payment, transactions);
  }
}
