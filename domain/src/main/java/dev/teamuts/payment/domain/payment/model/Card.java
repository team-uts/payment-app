package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.payment.constant.CardStatus;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto.ExtPGCardDetail;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto.ExtPGPaymentMethodDetail;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@DomainModel
@SuperBuilder(toBuilder = true)
@Getter
public class Card extends PaymentMethodDetail {
  private Long id;
  private Long memberId;
  private String last4;
  private String brand;
  private Integer expMonth;
  private Integer expYear;
  private CardStatus status;

  public static Card newActiveCard(PaymentMethod paymentMethod, ExtPGPaymentMethodDetail detail) {
    if (paymentMethod.getMethodType() != detail.methodType()) {
      throw new IllegalArgumentException(
          "PaymentMethodType mismatch: PaymentMethod (%s) - ExtDetail (%s)"
              .formatted(paymentMethod.getMethodType(), detail.methodType()));
    }

    if (!(detail instanceof ExtPGCardDetail cardDetail)) {
      throw new IllegalArgumentException("Invalid detail type for Card: " + detail.getClass());
    }

    return Card.builder()
        .memberId(paymentMethod.getMemberId())
        .payMethodId(paymentMethod.getId())
        .last4(cardDetail.getLast4())
        .brand(cardDetail.getBrand())
        .expMonth(cardDetail.getExpMonth())
        .expYear(cardDetail.getExpYear())
        .status(CardStatus.ACTIVE)
        .build();
  }

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
