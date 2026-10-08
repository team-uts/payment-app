package dev.teamuts.payment.domain.payment.facade.pay;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGPayTransactionProcessor implements PayTransactionProcessor {
  @Override
  public boolean supports(PaySequenceType key) {
    return key == PaySequenceType.PG_PAY;
  }

  @Override
  public PaymentTransaction pay(PaymentTransaction transaction) {
    return null;
  }
}
