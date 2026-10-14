package dev.teamuts.payment.domain.payment.facade.pay;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.pg.model.ExtPgPaymentTransaction;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.service.PGAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGPayTransactionProcessor implements PayTransactionProcessor {
  private final PayTransactionHelperProcessor payTransactionHelperProcessor;
  private final PGAccountService pgAccountService;
  private final PaymentMethodService paymentMethodService;

  @Override
  public boolean supports(PaySequenceType key) {
    return key == PaySequenceType.PG_PAY;
  }

  /**
   * Ultimately, this process aims to confirm the payment.
   *
   * @param transaction the payment transaction to process
   * @return the processed payment transaction (the argument tx will also be modified)
   */
  @Override
  public PaymentTransaction pay(PaymentTransaction transaction) {
    // 1. retrieve tx from PG to check if the transaction is already created.
    payTransactionHelperProcessor.callPgWithConditionalSync(
        transaction,
        pgService -> pgService.retrievePGPaymentTransaction(transaction),
        ExtPgPaymentTransaction::isNotInvalidRequest);

    // 2. Check if the transaction can continue processing PG pay.
    if (transaction.cannotContinueProcessingPgPay()) {
      return transaction;
    }

    // 3. In case app tx is not associated with PG tx
    if (transaction.requiresPgTransaction()) {
      // 3-1. Retrieve PGAccount to create new PG tx
      PGAccount pgAccount = pgAccountService.getPGAccountByMemberId(transaction.getMemberId());

      // 3-2. Retrieve PaymentMethod to create new PG tx
      PaymentMethod paymentMethod =
          paymentMethodService.getActivePaymentMethodById(transaction.getPayMethodId());

      // 3-3. Create new PG tx
      payTransactionHelperProcessor.callPgWithSync(
          transaction,
          pgService -> pgService.createPGTransaction(transaction, pgAccount, paymentMethod));
    }

    // 4. Only in case the PG tx can be confirmed, confirm it.
    if (transaction.canBeConfirmed()) {
      payTransactionHelperProcessor.callPgWithSync(
          transaction, pgService -> pgService.confirmPayment(transaction));
    }

    return transaction;
  }
}
