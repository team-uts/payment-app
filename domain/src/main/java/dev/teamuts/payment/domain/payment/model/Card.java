package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.payment.constant.CardStatus;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodDto.ExtPGCardDetail;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodDto.ExtPGPaymentMethodDetail;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.constant.ModelSourceType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Getter
public class Card extends PaymentMethodDetail {
  private Long memberId;
  private String last4;
  private String brand;
  private Integer expMonth;
  private Integer expYear;
  private CardStatus status;

  private static final String STR_EXP_MONTH_FORMAT = "%02d";

  @Builder(access = AccessLevel.PRIVATE, toBuilder = true)
  private Card(
      @Builder.ObtainVia(method = "getId") Long id,
      @Builder.ObtainVia(method = "getSourceType") ModelSourceType sourceType,
      Long payMethodId,
      Long memberId,
      String last4,
      String brand,
      Integer expMonth,
      Integer expYear,
      CardStatus status) {
    super(id, sourceType, payMethodId);
    this.memberId = memberId;
    this.last4 = last4;
    this.brand = brand;
    this.expMonth = expMonth;
    this.expYear = expYear;
    this.status = status;
  }

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
        .sourceType(ModelSourceType.LOGIC)
        .payMethodId(paymentMethod.getId())
        .memberId(paymentMethod.getMemberId())
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
        .sourceType(ModelSourceType.DATABASE)
        .payMethodId(payMethodId)
        .memberId(memberId)
        .last4(last4)
        .brand(brand)
        .expMonth(expMonth)
        .expYear(expYear)
        .status(status)
        .build();
  }

  public String getFormattedExpMonth() {
    return String.format(STR_EXP_MONTH_FORMAT, this.expMonth);
  }

  public Card copy() {
    return toBuilder().build();
  }
}
