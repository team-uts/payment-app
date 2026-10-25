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

  public void executeProcessing(PaymentTransaction transaction) {
    if (transaction.isNotProcessable()) {
      log.info(
          "Transaction (id: {}) already processed ({})",
          transaction.getId(),
          transaction.getStatus());

      return;
    }

    try {
      // execute Pay based on the pay type (PG or Point)
      payTxProcessorProvider.getInstance(transaction.getSequenceType()).pay(transaction);

      log.info(
          "Transaction (id: {}) processed successfully with status: {}",
          transaction.getId(),
          transaction.getStatus());
    } catch (Exception e) {
      // In case of PG API exception
      if (e instanceof ExtPGApiException extException) {
        log.error(
            "(External) Error payment transaction - txId: {} / pgResponseCode: {} / pgErrorCode: {} / pgErrorMessage: {}",
            transaction.getId(),
            extException.getPgResponseCode(),
            extException.getPgErrorCode(),
            extException.getPgErrorMessage());

        // pass, because the PG API exception's already been applied in the pay process.
        return;
      }

      log.error("(App) Error payment transaction - txId: {}", transaction.getId(), e);
      // In case of app exception
      paymentTransactionService.updateWithAppException(transaction, e);
    }
  }
}
