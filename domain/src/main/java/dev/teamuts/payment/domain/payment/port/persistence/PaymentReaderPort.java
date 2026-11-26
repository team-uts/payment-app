package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.Payments;
import java.util.Optional;

public interface PaymentReaderPort {
  Optional<Payment> retrievePaymentByOrderIdAndServiceType(
      Long orderId, AppServiceType serviceType, PaymentMethodType payMethodType);

  Payments retrieveAllByOrderIdWithOutboxEvent(Long orderId);
}
