package dev.teamuts.payment.domain.payment.facade.pay;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

@Component
@RequiredArgsConstructor
public class PointPayTransactionProcessor implements PayTransactionProcessor {
  @Override
  public boolean supports(PaySequenceType key) {
    return key == PaySequenceType.POINT;
  }

  @Override
  @AppTransactional(propagation = Propagation.NOT_SUPPORTED)
  public void pay(PaymentTransaction transaction) {
    // TODO: Implement point payment processing logic here
  }
}
