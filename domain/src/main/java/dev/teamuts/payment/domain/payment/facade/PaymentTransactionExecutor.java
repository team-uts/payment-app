package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.common.exception.ExtPGApiException;
import dev.teamuts.payment.domain.payment.facade.pay.PayTransactionProcessorProvider;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionExecutor {
  private final PayTransactionProcessorProvider payTxProcessorProvider;
  private final PaymentTransactionService paymentTransactionService;

  public void processTransaction(PaymentTransaction transaction) {
    if (transaction.isNotProcessable()) {
      throw new RuntimeException(
          "Transaction %s cannot process transaction".formatted(transaction.getId()));
    }

    try {
      // execute Pay based on the pay type (PG or Point)
      payTxProcessorProvider.getInstance(transaction.getSequenceType()).pay(transaction);
    } catch (Exception e) {
      log.error("Error occurred while processing payment transaction: {}", transaction.getId(), e);

      // In case of PG API exception
      if (e instanceof ExtPGApiException) {
        // pass, because the PG API exception's already been applied in the pay process.
        return;
      }

      // In case of app exception
      paymentTransactionService.updateWithAppException(transaction, e);
    }
  }
}
