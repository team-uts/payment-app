package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;

public interface PaymentTransactionUpdatePort {
  void updatePGTransactionInfo(PaymentTransaction transaction);

  void updateDetailMessage(PaymentTransaction transaction);
}
