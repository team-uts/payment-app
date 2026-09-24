package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.persistence.payment.entity.PaymentMethodJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodConverter {
  public PaymentMethod covertToDomainModel(PaymentMethodJpaEntity entity) {
    return PaymentMethod.fromDatabase(
        entity.getId(),
        entity.getMemberId(),
        entity.getPgProvider(),
        entity.getProviderToken(),
        entity.getMethodType(),
        entity.getDefaultMethod(),
        entity.getStatus());
  }
}
