package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.Card;

public interface CardStorePort {
  Card store(Card card);
}
