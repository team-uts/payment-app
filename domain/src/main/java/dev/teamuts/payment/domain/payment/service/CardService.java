package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.port.persistence.CardStorePort;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto.ExtPGPaymentMethodDetail;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
  private final CardStorePort cardStorePort;

  @AppTransactional
  public Card registerNewCard(PaymentMethod paymentMethod, ExtPGPaymentMethodDetail detail) {
    Card card = Card.newActiveCard(paymentMethod, detail);

    return cardStorePort.store(card);
  }
}
