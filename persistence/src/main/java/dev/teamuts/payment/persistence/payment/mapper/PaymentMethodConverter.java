package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.persistence.payment.dto.PaymentMethodListRow;
import dev.teamuts.payment.persistence.payment.entity.PaymentMethodJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodConverter {
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
    Card card =
        Card.fromDatabase(
            row.getCardId(),
            row.getMemberId(),
            row.getId(),
            row.getCardLastFour(),
            row.getCardBrand(),
            row.getCardExpiryMonth(),
            row.getCardExpiryYear(),
            row.getCardStatus());

    return PaymentMethod.fromDatabase(
        row.getId(),
        row.getMemberId(),
        row.getPgProvider(),
        null,
        row.getMethodType(),
        row.getDefaultMethod(),
        row.getStatus(),
        card);
  }
}
