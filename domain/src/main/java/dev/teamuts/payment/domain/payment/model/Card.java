package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.payment.constant.CardStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@DomainModel
@SuperBuilder
@Getter
public class Card extends PaymentMethodDetail {
  private Long id;
  private Long memberId;
  private String last4;
  private String brand;
  private Integer expMonth;
  private Integer expYear;
  private CardStatus status;

  public static Card fromDatabase(
      Long id,
      Long memberId,
      Long payMethodId,
      String last4,
      String brand,
      Integer expMonth,
      Integer expYear,
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
