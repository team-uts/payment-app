package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentStorePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import dev.teamuts.payment.persistence.payment.mapper.PaymentConverter;
import dev.teamuts.payment.persistence.payment.repository.PaymentRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class PaymentStoreAdapter implements PaymentStorePort {
  private final PaymentRepository paymentRepository;
  private final PaymentConverter paymentConverter;

  @Override
  public Payment store(Payment payment) {
    PaymentJpaEntity entity = paymentConverter.convertToJpaEntity(payment);

    PaymentJpaEntity savedEntity = paymentRepository.save(entity);

    return paymentConverter.convertToDomainModel(savedEntity);
  }
}
