package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {
  public PaymentJpaEntity convertToJpaEntity(Payment model) {
    return PaymentJpaEntity.fromDomain(model);
  }

  public Payment convertToDomainModel(PaymentJpaEntity entity) {
    return Payment.fromDatabase(
        entity.getId(),
        entity.getOrderId(),
        entity.getMemberId(),
        entity.getServiceType(),
        entity.getPaymentType(),
        entity.getStatus(),
        entity.getPgProvider(),
        entity.getPayAmount(),
        entity.getPointAmount(),
        entity.getCurrency(),
        entity.getPayMethodId(),
        entity.getPayMethodType(),
        entity.getOutboxEventId());
  }

  public Payment convertToDomainModelWithFetch(
      PaymentJpaEntity entity, List<PaymentTransaction> transactions) {
    return Payment.fromDatabaseWithTransactions(
        entity.getId(),
        entity.getOrderId(),
        entity.getMemberId(),
        entity.getServiceType(),
        entity.getPaymentType(),
        entity.getStatus(),
        entity.getPgProvider(),
        entity.getPayAmount(),
        entity.getPointAmount(),
        entity.getCurrency(),
        entity.getPayMethodId(),
        entity.getPayMethodType(),
        entity.getOutboxEventId(),
        transactions);
  }
}
