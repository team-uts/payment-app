package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.payment.facade.pay.PayTransactionProcessorProvider;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentTransactionFacade {
  private final PaymentTransactionService paymentTransactionService;
  private final PayTransactionProcessorProvider payTxProcessorProvider;

  public PaymentTransactions processPayment(Payment payment) {
    PaymentTransactions transactions = paymentTransactionService.createInitTransactions(payment);

    // TODO: add new Facade
    return transactions.processInSequence(
        transaction -> {
          PaymentTransaction resultTx =
              payTxProcessorProvider.getInstance(transaction.getSequenceType()).pay(transaction);

          return resultTx;
        });
  }
}
