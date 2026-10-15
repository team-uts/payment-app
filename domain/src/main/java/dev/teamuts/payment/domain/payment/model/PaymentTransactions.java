package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentTransactions {
  private List<PaymentTransaction> transactions;

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
   * for every sequence type applicable to this payment, skip the others
   *
   * @param payment target payment to process
   * @return new INIT transactions for every applicable sequence type
   */
  public static PaymentTransactions initFromPayment(Payment payment) {
    if (payment.cannotProcessPayTransaction()) {
      throw new RuntimeException("Payment cannot process pay transaction: " + payment.getId());
    }

    // initialize new tx based on the pay sequence type condition.
    List<PaymentTransaction> transactions =
        Arrays.stream(PaySequenceType.values())
            .sorted(Comparator.comparingInt(PaySequenceType::getSeqNumber))
            .filter(seqType -> seqType.isApplicableTo(payment))
            .map(seqType -> seqType.initTransaction(payment))
            .toList();

    return of(transactions);
  }

  public static PaymentTransactions empty() {
    return new PaymentTransactions(Collections.emptyList());
  }

  public void processInSequence(Consumer<PaymentTransaction> processor) {
    for (PaymentTransaction transaction : transactions) {
      processor.accept(transaction);

      // if the tx is not completed, stop processing the next txs.
      if (transaction.isNotCompleted()) {
        break;
      }
    }
  }

  public <E> List<E> mapToList(Function<PaymentTransaction, E> mapper) {
    return transactions.stream().map(mapper).toList();
  }

  public PaymentStatus resolvePaymentStatus() {
    return PaymentStatusRuleResolver.resolve(transactions);
  }

  public boolean isEmpty() {
    return transactions.isEmpty();
  }

  public boolean isNotEmpty() {
    return !isEmpty();
  }
}
