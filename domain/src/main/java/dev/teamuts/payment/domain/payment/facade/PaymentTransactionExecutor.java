package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.payment.facade.pay.PayTransactionProcessorProvider;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import dev.teamuts.payment.shared.data.AppTransactional;
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

  /** TODO: Not considering PENDING status at the moment, it should be developed in the future. */
  @AppTransactional(propagation = Propagation.REQUIRES_NEW)
  public PaymentTransaction processTransaction(PaymentTransaction transaction) {
    if (transaction.isNotProcessable()) {
      throw new RuntimeException(
          "Transaction %s cannot process transaction".formatted(transaction.getId()));
    }

    PaymentTransaction resultTx = process(transaction);

    paymentTransactionService.updateStatus(resultTx);

    return resultTx;
  }

  private PaymentTransaction process(PaymentTransaction transaction) {
    try {
      // execute Pay based on the pay type (PG or Point)
      return payTxProcessorProvider.getInstance(transaction.getSequenceType()).pay(transaction);
    } catch (Exception e) {
      log.error("Error occurred while processing payment transaction: {}", transaction.getId(), e);

      transaction.failed();

      return transaction;
    }
  }
}
