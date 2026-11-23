package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import dev.teamuts.payment.persistence.payment.entity.PaymentTransactionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentTransactionConverter {
  public PaymentTransactionJpaEntity convertToJpaEntity(
      PaymentTransaction model, PaymentJpaEntity paymentEntity) {
    return PaymentTransactionJpaEntity.newEntity(model, paymentEntity);
  }

  public PaymentTransaction convertToDomainModel(PaymentTransactionJpaEntity entity) {
    return PaymentTransaction.fromDatabase(
        entity.getId(),
        entity.getMemberId(),
        entity.getPayment().getId(),
        entity.getOrderId(),
        entity.getPayMethodId(),
        entity.getPayMethodType(),
        entity.getServiceType(),
        entity.getPaymentType(),
        entity.getAmount(),
        entity.getCurrency(),
        entity.getPgProvider(),
        entity.getPgTransactionId(),
        entity.getPgResponseCode(),
        entity.getPgErrorCode(),
        entity.getPgErrorMessage(),
        entity.getDetailMessage(),
        entity.getStatus(),
        entity.getSequence());
  }
}
