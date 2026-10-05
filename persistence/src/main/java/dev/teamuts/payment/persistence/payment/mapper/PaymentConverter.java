package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {
  public PaymentJpaEntity convertToJpaEntity(Payment payment) {
    return PaymentJpaEntity.fromDomain(payment);
  }

  public Payment convertToDomainModel(PaymentJpaEntity paymentJpaEntity) {
    return Payment.fromDatabase(
        paymentJpaEntity.getId(),
        paymentJpaEntity.getOrderId(),
        paymentJpaEntity.getMemberId(),
        paymentJpaEntity.getServiceType(),
        paymentJpaEntity.getStatus(),
        paymentJpaEntity.getPayAmount(),
        paymentJpaEntity.getPointAmount(),
        paymentJpaEntity.getCurrency(),
        paymentJpaEntity.getPayMethodId(),
        paymentJpaEntity.getPayMethodType());
  }
}
