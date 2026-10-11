package dev.teamuts.payment.domain.payment.facade.pay;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.service.ExternalPGServiceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGPayTransactionProcessor implements PayTransactionProcessor {
  private final ExternalPGServiceProvider pgServiceProvider;

  @Override
  public boolean supports(PaySequenceType key) {
    return key == PaySequenceType.PG_PAY;
  }

  @Override
  public PaymentTransaction pay(PaymentTransaction transaction) {
    // 1. Check if the transaction is already created (retrieve from PG).
    pgServiceProvider.getInstance(transaction.getPgProvider()).confirmPaymentRequest();

    // 1-1. If PG tx exists, change status and return it
    // 1) PG tx == COMPLETED > change the App tx status to COMPLETED
    // 2) PG tx == ANOTHER_STATUS > change the App tx status to the mapped status

    // If no PG transaction, create new PG tx and store it in DB.

    return null;
  }
}
