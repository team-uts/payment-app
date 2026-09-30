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
  private CardDetailRow cardDetail;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CardDetailRow {
    Long id;
    Long memberId;
    String brand;
    String last4;
    Integer expiryMonth;
    Integer expiryYear;
    CardStatus status;
  }
}
