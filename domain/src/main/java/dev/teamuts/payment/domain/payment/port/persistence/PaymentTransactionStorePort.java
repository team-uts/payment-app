package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.PaymentTransactions;

public interface PaymentTransactionStorePort {
  PaymentTransactions storeAll(PaymentTransactions transactions);
}
