package dev.teamuts.payment.persistence.payment.mapper;

import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.persistence.payment.entity.CardJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {
  public CardJpaEntity convertToJpaEntity(Card card) {
    return CardJpaEntity.fromDomain(card);
  }

  public Card convertToDomainModel(CardJpaEntity cardJpaEntity) {
    return Card.fromDatabase(
        cardJpaEntity.getId(),
        cardJpaEntity.getMemberId(),
        cardJpaEntity.getPayMethodId(),
        cardJpaEntity.getLastFour(),
        cardJpaEntity.getBrand(),
        cardJpaEntity.getExpiryMonth(),
        cardJpaEntity.getExpiryYear(),
        cardJpaEntity.getStatus());
  }
}
