package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentTransactionReaderPort;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentTransactionStorePort;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentTransactionUpdatePort;
import dev.teamuts.payment.domain.pg.model.ExtPgPaymentTransaction;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {
  private final PaymentTransactionStorePort paymentTransactionStorePort;
  private final PaymentTransactionReaderPort paymentTransactionReaderPort;
  private final PaymentTransactionUpdatePort paymentTransactionUpdatePort;

  @AppTransactional
  public PaymentTransactions getTransactionsOrCreateInit(Payment payment) {
    PaymentTransactions transactions =
        paymentTransactionReaderPort.retrieveAllByPaymentId(payment.getId());

    // If transactions already exist, return them without creating new INIT txs
    if (transactions.isNotEmpty()) {
      return transactions;
    }

    // Create an INIT tx from payment
    PaymentTransactions newTransactions = PaymentTransactions.initFromPayment(payment);

    // Store all new txs
    return paymentTransactionStorePort.storeAll(newTransactions);
  }

  @AppTransactional
  public void updateWithPGResult(PaymentTransaction transaction, ExtPgPaymentTransaction pgResult) {
    transaction.updatePGTransactionInfo(pgResult);

    paymentTransactionUpdatePort.updatePGTransactionInfo(transaction);
  }

  @AppTransactional
  public void updateWithAppException(PaymentTransaction transaction, Exception exception) {
    transaction.updateWithAppException(exception);

    paymentTransactionUpdatePort.updateDetailMessage(transaction);
  }
}
