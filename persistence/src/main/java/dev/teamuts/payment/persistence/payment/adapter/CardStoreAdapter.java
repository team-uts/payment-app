package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.domain.payment.port.persistence.CardStorePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.entity.CardJpaEntity;
import dev.teamuts.payment.persistence.payment.mapper.CardConverter;
import dev.teamuts.payment.persistence.payment.repository.CardRepository;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CardStoreAdapter implements CardStorePort {
  private final CardRepository cardRepository;
  private final CardConverter cardConverter;

  @Override
  public Card store(Card card) {
    CardJpaEntity entity = cardConverter.convertToJpaEntity(card);

    CardJpaEntity savedEntity = cardRepository.save(entity);

    return cardConverter.convertToDomainModel(savedEntity);
  }
}
