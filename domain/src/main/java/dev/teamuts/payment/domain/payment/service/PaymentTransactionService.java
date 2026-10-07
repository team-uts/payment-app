package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.port.PaymentTransactionStorePort;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {
  private final PaymentTransactionStorePort paymentTransactionStorePort;

  @AppTransactional
  public List<PaymentTransaction> createInitTransactions(Payment payment) {
    List<PaymentTransaction> transactions = new ArrayList<>();

    if (payment.hasPayAmount()) {
      transactions.add(PaymentTransaction.initPay(payment));
    }

    if (payment.hasPointAmount()) {
      transactions.add(PaymentTransaction.initPoint(payment));
    }

    return paymentTransactionStorePort.storeAll(transactions);
  }
}
