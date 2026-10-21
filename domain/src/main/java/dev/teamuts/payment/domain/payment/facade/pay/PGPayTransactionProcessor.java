package dev.teamuts.payment.domain.payment.facade.pay;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.facade.pay.helper.PayProcessStepPipeline;
import dev.teamuts.payment.domain.payment.facade.pay.helper.PayTransactionHelperProcessor;
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
   * 1. check the previously created PG tx.
   * 2. create new PG tx.
   * 3. confirm the PG tx. >> require to handle ReadTimeout error (PENDING)
   *
   * @param transaction the payment transaction to process
   * @return the processed payment transaction (the argument tx will also be modified)
   */
  @Override
  public PaymentTransaction pay(PaymentTransaction transaction) {
    // 1. retrieve tx from PG to check if the transaction is already created.
    // if the tx is already associated with PG, then execute synchronization with PG again.
    return PayProcessStepPipeline.start(transaction)
        .runIf(
            PaymentTransaction::isAlreadyAssociatedWithPg,
            tx -> {
              // when the PG tx is not found (invalid_request), the PG tx will not be updated to DB
              payTransactionHelperProcessor.callPgWithConditionalSync(
                  tx,
                  pgService -> pgService.retrievePGPaymentTransaction(tx),
                  ExtPgPaymentTransaction::isNotInvalidRequest);
            })
        // 2. Check if the transaction can continue processing PG pay.
        // (INIT, CREATED only permitted here to continue)
        .stopIf(PaymentTransaction::cannotContinueProcessingPgPay)
        // 3. In case app tx is not associated with PG tx
        .runIf(
            PaymentTransaction::requiresPgAssociation,
            tx -> {
              // 3-1. Retrieve PGAccount to create new PG tx
              PGAccount pgAccount = pgAccountService.getPGAccountByMemberId(tx.getMemberId());

              // 3-2. Retrieve PaymentMethod to create new PG tx
              PaymentMethod paymentMethod =
                  paymentMethodService.getActivePaymentMethodById(tx.getPayMethodId());

              // 3-3. Create new PG tx
              payTransactionHelperProcessor.callPgWithSync(
                  tx,
                  pgService ->
                      pgService.createPGTransaction(transaction, pgAccount, paymentMethod));
            })
        // 4. Only in case the PG tx can be confirmed, confirm it.
        .runIf(
            PaymentTransaction::canBeConfirmed,
            tx -> {
              payTransactionHelperProcessor.callPgWithSync(
                  tx, pgService -> pgService.confirmPayment(tx));
            })
        .done();
  }
}
