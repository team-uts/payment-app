package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentTransactions {
  private List<PaymentTransaction> transactions;

  public static PaymentTransactions of(List<PaymentTransaction> paymentTransactions) {
    if (paymentTransactions.isEmpty()) {
      throw new IllegalArgumentException("paymentTransactions must not be empty");
    }

    return new PaymentTransactions(paymentTransactions);
  }

  public PaymentTransactions processInSequence(
      Function<PaymentTransaction, PaymentTransaction> processor) {
    List<PaymentTransaction> resultTransactions =
        transactions.stream()
            .sorted((Comparator.comparingInt(tx -> tx.getSequenceType().getSeqNumber())))
            .map(processor)
            .toList();

    return new PaymentTransactions(resultTransactions);
  }

  public PaymentStatus resolvePaymentStatus() {
    boolean isAllCompleted = transactions.stream().allMatch(PaymentTransaction::isCompleted);

    if (isAllCompleted) {
      return PaymentStatus.COMPLETED;
    }

    boolean hasAnyFailed = transactions.stream().anyMatch(PaymentTransaction::isFailed);

    if (hasAnyFailed) {
      return PaymentStatus.FAILED;
    }

    boolean hasAnyPending = transactions.stream().anyMatch(PaymentTransaction::isFailed);

    if (hasAnyPending) {
      return PaymentStatus.PENDING;
    }

    return PaymentStatus.PROCESSING;
  }
}
