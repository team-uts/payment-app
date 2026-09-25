package dev.teamuts.payment.persistence.payment.dto;

import dev.teamuts.payment.domain.payment.constant.CardStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodListRow {
  private Long id;
  private Long memberId;
  private PGProviderType pgProvider;
  private PaymentMethodType methodType;
  private Boolean defaultMethod;
  private PaymentMethodStatus status;
  private Long cardId;
  private Long cardMemberId;
  private String cardBrand;
  private String cardLastFour;
  private Integer cardExpiryMonth;
  private Integer cardExpiryYear;
  private CardStatus cardStatus;
}
