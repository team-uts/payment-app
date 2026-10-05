package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.Payment;

public interface PaymentStorePort {
  Payment store(Payment payment);
}
