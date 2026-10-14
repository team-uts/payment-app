package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.PaymentTransactions;

public interface PaymentTransactionReaderPort {
  PaymentTransactions retrieveAllByPaymentId(Long paymentId);
}
