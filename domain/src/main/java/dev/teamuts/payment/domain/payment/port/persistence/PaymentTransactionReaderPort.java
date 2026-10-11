package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import java.util.List;

public interface PaymentTransactionReaderPort {
  List<PaymentTransaction> retrieveAllByPaymentId(Long paymentId);
}
