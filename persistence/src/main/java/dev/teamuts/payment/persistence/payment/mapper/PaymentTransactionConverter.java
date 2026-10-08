package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.persistence.payment.entity.PaymentTransactionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentTransactionConverter {
  public PaymentTransactionJpaEntity convertToJpaEntity(PaymentTransaction model) {
    return PaymentTransactionJpaEntity.newEntity(model);
  }

  public PaymentTransaction convertToDomainModel(PaymentTransactionJpaEntity entity) {
    return PaymentTransaction.fromDatabase(
        entity.getId(),
        entity.getMemberId(),
        entity.getPaymentId(),
        entity.getOrderId(),
        entity.getPayMethodId(),
        entity.getPayMethodType(),
        entity.getServiceType(),
        entity.getAmount(),
        entity.getCurrency(),
        entity.getPgProvider(),
        entity.getPgTransactionId(),
        entity.getPgResponseMessage(),
        entity.getStatus(),
        entity.getSequence());
  }
}
