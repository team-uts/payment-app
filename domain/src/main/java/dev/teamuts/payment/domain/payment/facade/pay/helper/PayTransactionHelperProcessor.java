package dev.teamuts.payment.domain.payment.facade.pay.helper;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import dev.teamuts.payment.domain.pg.error.ExtPgApiException;
import dev.teamuts.payment.domain.pg.model.sub.ExtPgPaymentTransaction;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPgPaymentServicePort;
import dev.teamuts.payment.domain.pg.service.ExtPGPaymentServiceProvider;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayTransactionHelperProcessor {
  private final ExtPGPaymentServiceProvider pgPaymentServiceProvider;
  private final PaymentTransactionService paymentTransactionService;

  public void callPgWithConditionalSync(
      PaymentTransaction transaction,
      Function<ExternalPgPaymentServicePort, ExtPgPaymentTransaction> pgCall,
      Predicate<ExtPgPaymentTransaction> doSyncCondition) {
    // call PG API service
    ExtPgPaymentTransaction pgTransaction =
        pgCall.apply(pgPaymentServiceProvider.getInstance(transaction.getPgProvider()));

    if (doSyncCondition == null || doSyncCondition.test(pgTransaction)) {
      // Update the pg tx result in PaymentTransaction domain.
      paymentTransactionService.updateWithPGResult(transaction, pgTransaction);
    }

    // If PG tx call was failed, throw exception to stop the process.
    if (pgTransaction.isFailed()) {
      throw ExtPgApiException.transactionFailed(pgTransaction);
    }
  }

  public void callPgWithSync(
      PaymentTransaction transaction,
      Function<ExternalPgPaymentServicePort, ExtPgPaymentTransaction> pgCall) {
    callPgWithConditionalSync(transaction, pgCall, null);
  }
}
