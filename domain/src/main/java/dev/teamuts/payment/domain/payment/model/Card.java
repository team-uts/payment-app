package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.payment.constant.CardStatus;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodDto.ExtPGCardDetail;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodDto.ExtPGPaymentMethodDetail;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Builder(access = lombok.AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class Card extends PaymentMethodDetail {
  private Long memberId;
  private String last4;
  private String brand;
  private Integer expMonth;
  private Integer expYear;
  private CardStatus status;

  private static final String STR_EXP_MONTH_FORMAT = "%02d";

  public static Card newActiveCard(PaymentMethod paymentMethod, ExtPGPaymentMethodDetail detail) {
    if (paymentMethod.getMethodType() != detail.methodType()) {
      throw new IllegalArgumentException(
          "PaymentMethodType mismatch: PaymentMethod (%s) - ExtDetail (%s)"
              .formatted(paymentMethod.getMethodType(), detail.methodType()));
    }

    if (!(detail instanceof ExtPGCardDetail cardDetail)) {
      throw new IllegalArgumentException("Invalid detail type for Card: " + detail.getClass());
    }

    Card card =
        Card.builder()
            .memberId(paymentMethod.getMemberId())
            .last4(cardDetail.getLast4())
            .brand(cardDetail.getBrand())
            .expMonth(cardDetail.getExpMonth())
            .expYear(cardDetail.getExpYear())
            .status(CardStatus.ACTIVE)
            .build();

    card.payMethodId = paymentMethod.getId();

    return card;
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
    Card card =
        Card.builder()
            .memberId(memberId)
            .last4(last4)
            .brand(brand)
            .expMonth(expMonth)
            .expYear(expYear)
            .status(status)
            .build();

    card.id = id;
    card.payMethodId = payMethodId;

    return card;
  }

  public String getFormattedExpMonth() {
    return String.format(STR_EXP_MONTH_FORMAT, this.expMonth);
  }

  public Card copy() {
    Card copy = this.toBuilder().build();
    copy.id = this.id;
    copy.payMethodId = this.payMethodId;

    return copy;
  }
}
