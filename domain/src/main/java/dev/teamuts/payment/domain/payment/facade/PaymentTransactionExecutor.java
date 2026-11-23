package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.payment.facade.pay.PayTransactionProcessorProvider;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import dev.teamuts.payment.domain.pg.error.ExtPgApiException;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionExecutor {
  private final PayTransactionProcessorProvider payTxProcessorProvider;
  private final PaymentTransactionService paymentTransactionService;

  @AppTransactional(propagation = Propagation.NOT_SUPPORTED)
  public void executeProcessing(PaymentTransaction transaction) {
    // if the tx is already processed (COMPLETED or CANCELED), skip the processing.
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
    } catch (ExtPgApiException e) {
      // In case of PG API exception
      // pass, because the PG API exception's already been applied in the pay process.
      log.error(
          "(External) Error payment transaction - txId: {} / pgResponseCode: {} / pgErrorCode: {} / pgErrorMessage: {}",
          transaction.getId(),
          e.getPgResponseCode(),
          e.getPgErrorCode(),
          e.getPgErrorMessage());
    } catch (Exception e) {
      log.error("(App) Error payment transaction - txId: {}", transaction.getId(), e);
      // In case of app exception
      paymentTransactionService.updateWithAppException(transaction, e);
    }
  }
}
