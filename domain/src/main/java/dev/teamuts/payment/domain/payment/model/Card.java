package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.payment.constant.CardStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Card {
  private Long id;
  private Long memberId;
  private String payMethodId;
  private String last4;
  private String brand;
  private String expMonth;
  private String expYear;
  private CardStatus status;

  public static Card fromDatabase(
      Long id,
      Long memberId,
      String payMethodId,
      String last4,
      String brand,
      String expMonth,
      String expYear,
      CardStatus status) {
    return Card.builder()
        .id(id)
        .memberId(memberId)
        .payMethodId(payMethodId)
        .last4(last4)
        .brand(brand)
        .expMonth(expMonth)
        .expYear(expYear)
        .status(status)
        .build();
  }
}
