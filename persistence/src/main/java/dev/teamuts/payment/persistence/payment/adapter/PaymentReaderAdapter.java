package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentReaderPort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.mapper.PaymentConverter;
import dev.teamuts.payment.persistence.payment.repository.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentReaderAdapter implements PaymentReaderPort {
  private final PaymentRepository paymentRepository;
  private final PaymentConverter paymentConverter;

  @Override
  public Optional<Payment> retrievePaymentByOrderIdAndServiceType(
      Long orderId, AppServiceType serviceType, PaymentMethodType payMethodType) {
    return paymentRepository
        .findByOrderIdAndServiceTypeAndPayMethodType(orderId, serviceType, payMethodType)
        .map(paymentConverter::convertToDomainModel);
  }
}
