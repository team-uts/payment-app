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
public class PaymentTransaction {
  private Long id;
  private Long memberId;
  private Long paymentId;
  private Long orderId;
  private Long payMethodId;
  private PaymentMethodType payMethodType;
  private AppServiceType serviceType;
  private BigDecimal amount;
  private Currency currency;
  private PGProviderType pgProvider;
  private String pgTransactionId;
  private String pgResponseMessage;
  private PaymentStatus status;
}
