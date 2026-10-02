package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;

public interface PaymentMethodStorePort {
  PaymentMethod store(PaymentMethod paymentMethod);
}
