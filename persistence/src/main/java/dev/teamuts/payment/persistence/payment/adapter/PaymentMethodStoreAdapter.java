package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentMethodStorePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.entity.PaymentMethodJpaEntity;
import dev.teamuts.payment.persistence.payment.mapper.PaymentMethodConverter;
import dev.teamuts.payment.persistence.payment.repository.PaymentMethodRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class PaymentMethodStoreAdapter implements PaymentMethodStorePort {
  private final PaymentMethodRepository paymentMethodRepository;
  private final PaymentMethodConverter paymentMethodConverter;

  @Override
  public PaymentMethod store(PaymentMethod paymentMethod) {
    PaymentMethodJpaEntity entity = paymentMethodConverter.convertToJpaEntity(paymentMethod);

    PaymentMethodJpaEntity savedEntity = paymentMethodRepository.save(entity);

    return paymentMethodConverter.convertToDomainModel(savedEntity);
  }
}
