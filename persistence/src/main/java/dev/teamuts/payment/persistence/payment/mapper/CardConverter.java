package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.persistence.payment.entity.CardJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {
  public CardJpaEntity convertToJpaEntity(Card model) {
    return CardJpaEntity.fromDomain(model);
  }

  public Card convertToDomainModel(CardJpaEntity entity) {
    return Card.fromDatabase(
        entity.getId(),
        entity.getMemberId(),
        entity.getPayMethodId(),
        entity.getLastFour(),
        entity.getBrand(),
        entity.getExpiryMonth(),
        entity.getExpiryYear(),
        entity.getStatus());
  }
}
