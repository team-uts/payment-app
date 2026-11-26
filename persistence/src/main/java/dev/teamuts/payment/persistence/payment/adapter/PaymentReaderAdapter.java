package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentReaderPort;
import dev.teamuts.payment.persistence.event.mapper.OutboxEventConverter;
import dev.teamuts.payment.persistence.payment.mapper.PaymentConverter;
import dev.teamuts.payment.persistence.payment.mapper.PaymentTransactionConverter;
import dev.teamuts.payment.persistence.payment.repository.PaymentRepository;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentReaderAdapter implements PaymentReaderPort {
  private final PaymentRepository paymentRepository;
  private final PaymentConverter paymentConverter;
  private final PaymentTransactionConverter paymentTransactionConverter;
  private final OutboxEventConverter outboxEventConverter;

  @Override
  public Optional<Payment> retrievePaymentByOrderIdAndServiceType(
      Long orderId, AppServiceType serviceType, PaymentMethodType payMethodType) {
    return paymentRepository
        .findByOrderIdAndServiceTypeAndPayMethodType(orderId, serviceType, payMethodType)
        .map(paymentConverter::convertToDomainModel);
  }

  @Override
  public Payments retrieveAllByOrderIdWithOutboxEvent(Long orderId) {
    List<Payment> payments =
        paymentRepository.findAllByOrderIdJoiningOutboxEvent(orderId).stream()
            .map(payment ->
                paymentConverter.convertToDomainModelWithFetch(
                    payment,
                    Collections.emptyList(),
                    payment.getOutboxEvent() == null
                        ? null
                        : outboxEventConverter.convertToDomainModel(payment.getOutboxEvent())))
            .toList();

    return Payments.of(payments);
  }
}
