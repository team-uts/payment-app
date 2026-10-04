package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class Payment {
  private Long id;
  private Long orderId;
  private Long memberId;
  private AppServiceType serviceType;
  private PaymentStatus status;
  private BigDecimal amount;
  private BigDecimal pointAmount;
  private Currency currency;
  private PGProviderType pgProvider;
  private Long payMethodId;
  private PaymentMethodType payMethodType;
}
