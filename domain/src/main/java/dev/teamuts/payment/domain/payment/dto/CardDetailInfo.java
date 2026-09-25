package dev.teamuts.payment.domain.payment.dto;

import dev.teamuts.payment.domain.payment.constant.CardStatus;
import dev.teamuts.payment.domain.payment.model.Card;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CardDetailInfo {
  private String last4;
  private String brand;
  private String expMonth;
  private String expYear;
  private CardStatus status;

  public static CardDetailInfo of(Card card) {
    return CardDetailInfo.builder()
        .last4(card.getLast4())
        .brand(card.getBrand())
        .expMonth(String.format("%02d", card.getExpMonth())) // TODO: formatting on other class
        .expYear(String.valueOf(card.getExpYear()))
        .status(card.getStatus())
        .build();
  }
}
