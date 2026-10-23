package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.persistence.payment.dto.PaymentMethodListRow;
import dev.teamuts.payment.persistence.payment.dto.PaymentMethodListRow.CardDetailRow;
import dev.teamuts.payment.persistence.payment.entity.PaymentMethodJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodConverter {
  public PaymentMethodJpaEntity convertToJpaEntity(PaymentMethod model) {
    return PaymentMethodJpaEntity.fromDomain(model);
  }

  public PaymentMethod convertToDomainModel(PaymentMethodJpaEntity entity) {
    return PaymentMethod.fromDatabase(
        entity.getId(),
        entity.getMemberId(),
        entity.getPgProvider(),
        entity.getProviderToken(),
        entity.getMethodType(),
        entity.getDefaultMethod(),
        entity.getStatus(),
        null);
  }

  public PaymentMethod convertToDomainModel(PaymentMethodListRow row) {
    CardDetailRow cardRow = row.getCardDetail();

    return PaymentMethod.fromDatabase(
        row.getId(),
        row.getMemberId(),
        row.getPgProvider(),
        null,
        row.getMethodType(),
        row.getDefaultMethod(),
        row.getStatus(),
        cardRow != null
            ? Card.fromDatabase(
                cardRow.getId(),
                cardRow.getMemberId(),
                row.getId(),
                cardRow.getLast4(),
                cardRow.getBrand(),
                cardRow.getExpiryMonth(),
                cardRow.getExpiryYear(),
                cardRow.getStatus())
            : null);
  }
}
