package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.model.Payment;
import java.util.Optional;

public interface PaymentReaderPort {
  Optional<Payment> retrievePaymentByOrderIdAndServiceType(
      Long orderId, AppServiceType serviceType, PaymentMethodType payMethodType);
}
