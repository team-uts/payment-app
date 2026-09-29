package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.shared.data.AppTransactional;
import org.springframework.stereotype.Service;

@Service
public class CardService {
  @AppTransactional
  public Card registerNewCard() {
    // Implement the logic to register a card
    return null;
  }
}
