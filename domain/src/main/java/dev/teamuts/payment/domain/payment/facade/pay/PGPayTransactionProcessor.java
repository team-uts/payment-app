package dev.teamuts.payment.domain.payment.facade.pay;

import dev.teamuts.payment.domain.common.process.DomainProcessStepPipeline;
import dev.teamuts.payment.domain.event.message.OrderEventMessage;
import dev.teamuts.payment.domain.event.service.OutboxEventPublisher;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.facade.pay.helper.PayTransactionHelperProcessor;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.pg.model.ExtPgPaymentTransaction;
import dev.teamuts.payment.domain.pg.model.PgAccount;
import dev.teamuts.payment.domain.pg.service.PgAccountService;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGPayTransactionProcessor implements PayTransactionProcessor {
  private final PayTransactionHelperProcessor payTransactionHelperProcessor;
  private final PgAccountService pgAccountService;
  private final PaymentMethodService paymentMethodService;
  private final OutboxEventPublisher outboxEventPublisher;

  @Override
  public boolean supports(PaySequenceType key) {
    return key == PaySequenceType.PG_PAY;
  }

  /**
   * Ultimately, this process aims to confirm the payment.<br>
   * 1. check the previously created PG tx.<br>
   * 2. create new PG tx.<br>
   * 3. confirm the PG tx. >> require to handle ReadTimeout error (PENDING)
   *
   * @param transaction the payment transaction to process
   */
  @Override
  public void pay(PaymentTransaction transaction) {
    // 1. retrieve tx from PG to check if the transaction is already created.
    // if the tx is already associated with PG, then execute synchronization with PG again.
    DomainProcessStepPipeline.start(transaction)
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
        .stopWhen(PaymentTransaction::cannotContinueProcessingPgPay)
        // 3. In case app tx is not associated with PG tx
        .runIf(
            PaymentTransaction::needsPgAssociation,
            tx -> {
              // 3-1. Retrieve PGAccount to create new PG tx
              PgAccount pgAccount = pgAccountService.getPGAccountByMemberId(tx.getMemberId());

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
            });
  }

  @Override
  @AppTransactional
  public void afterPay(PaymentTransaction transaction) {
    // delegate to batch processing to publish the outbox events again
    // if they were not processed yet.
    if (transaction.alreadyHasOutboxEvent()) {
      return;
    }

    outboxEventPublisher.publish(OrderEventMessage.PayCustomerOrder.of(transaction));
  }
}
