package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentTransactionReaderPort;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentTransactionStorePort;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {
  private final PaymentTransactionStorePort paymentTransactionStorePort;
  private final PaymentTransactionReaderPort paymentTransactionReaderPort;

  // TODO: Retrieve existing transactions from DB and create INIT tx if not exist
  @AppTransactional
  public PaymentTransactions getTransactionsOrCreateInit(Payment payment) {
    List<PaymentTransaction> transactions =
        paymentTransactionReaderPort.retrieveAllByPaymentId(payment.getId());

    // If transactions already exist, return them without creating new INIT txs
    if (!transactions.isEmpty()) {
      return PaymentTransactions.of(transactions);
    }

    // In case the payment has pay amount
    if (payment.hasPayAmount()) {
      transactions.add(PaymentTransaction.initPay(payment));
    }

    // In case the payment has point amount
    if (payment.hasPointAmount()) {
      transactions.add(PaymentTransaction.initPoint(payment));
    }

    // Store all new txs
    List<PaymentTransaction> storedTransactions =
        paymentTransactionStorePort.storeAll(transactions);

    return PaymentTransactions.of(storedTransactions);
  }

  @AppTransactional
  public void updateStatus(PaymentTransaction transaction) {}
}
