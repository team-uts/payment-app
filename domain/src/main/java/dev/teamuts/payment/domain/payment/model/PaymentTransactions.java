package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.error.PaymentException;
import dev.teamuts.payment.domain.payment.error.PaymentTransactionErrorCode;
import dev.teamuts.shared.domain.model.BaseIdentifiableModelCollection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class PaymentTransactions extends BaseIdentifiableModelCollection<Long, PaymentTransaction> {
  private PaymentTransactions(List<PaymentTransaction> transactions) {
    super(transactions);
  }

  public static PaymentTransactions ofOrEmpty(List<PaymentTransaction> paymentTransactions) {
    return new PaymentTransactions(
        paymentTransactions.stream()
            .sorted((Comparator.comparingInt(tx -> tx.getSequenceType().getSeqNumber())))
            .toList());
  }

  public static PaymentTransactions of(List<PaymentTransaction> paymentTransactions) {
    if (paymentTransactions.isEmpty()) {
      throw new IllegalArgumentException("paymentTransactions must not be empty");
    }

    return ofOrEmpty(paymentTransactions);
  }

  /**
   * create new init transactions from valid payment.
   *
   * @param payment target payment to process (only for {@link PaymentStatus#PROCESSING} status)
   * @return new INIT transactions for every applicable sequence type
   */
  public static PaymentTransactions initFromPayment(Payment payment) {
    if (!payment.isProcessing()) {
      throw PaymentException.of(PaymentTransactionErrorCode.CANNOT_GENERATE_TXS, payment.getId());
    }

    // initialize new tx based on the pay sequence type condition.
    // first, filter with the condition where the sequence type is applicable to this payment.
    List<PaymentTransaction> transactions =
        Arrays.stream(PaySequenceType.values())
            .filter(seqType -> seqType.isApplicableTo(payment))
            .sorted(Comparator.comparingInt(PaySequenceType::getSeqNumber))
            .map(seqType -> seqType.initTransaction(payment))
            .toList();

    return of(transactions);
  }

  public static PaymentTransactions empty() {
    return new PaymentTransactions(Collections.emptyList());
  }

  public void processInSequence(Consumer<PaymentTransaction> processor) {
    for (PaymentTransaction transaction : elements()) {
      processor.accept(transaction);

      // if the tx is not completed, stop processing the next txs.
      if (transaction.isNotCompleted()) {
        break;
      }
    }
  }

  public PaymentStatus resolvePaymentStatus() {
    return PaymentStatusRuleResolver.resolve(elements());
  }

  public Long getCommonPaymentId() {
    if (elements().isEmpty()) {
      throw new IllegalStateException("PaymentTransactions is empty");
    }

    List<Long> paymentIds =
        elements().stream().map(PaymentTransaction::getPaymentId).distinct().toList();

    if (paymentIds.size() > 1) {
      throw new IllegalStateException(
          "PaymentTransactions contains different paymentIds: " + paymentIds);
    }

    return paymentIds.getFirst();
  }
}
