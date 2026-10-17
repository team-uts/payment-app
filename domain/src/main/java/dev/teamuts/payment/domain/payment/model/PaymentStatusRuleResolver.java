package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Defines the rules that map the statuses {@link PaymentTransactionStatus} of a {@link
 * PaymentTransaction} to the status of the Payment itself.
 *
 * <p>Transactions are processed in sequence order {@link PaySequenceType} and <b>a transaction can
 * start only after the previous one {@link PaymentTransactionStatus#COMPLETED}</b>, so the sequence
 * always looks like {@code (COMPLETED, INIT|FAILED|PENDING|...)}. The Payment status is therefore
 * decided by a single <b>current step</b> that's the first non-COMPLETED transaction, plus whether
 * any earlier step was already done.
 *
 * <pre>
 * Cases: transaction statuses to payment status
 * 1. ({@code FAILED},          {@code INIT})             >> {@code FAILED}     nothing taken yet - the whole payment failed
 * 2. ({@code PENDING},         {@code INIT})             >> {@code PENDING}    PG result unknown (e.g. PG timeout)
 * 3. ({@code ACTION_REQUIRED}, {@code INIT})             >> {@code PENDING}    waiting on the payer (e.g. 3DS)
 * 4. ({@code COMPLETED},       {@code FAILED})           >> {@code PENDING}    partially taken - need to retry
 * 5. ({@code COMPLETED},       {@code PENDING})          >> {@code PENDING}    partially taken - need to retry
 * 6. ({@code COMPLETED},       {@code ACTION_REQUIRED})  >> {@code PENDING}    partially taken - need to retry
 * 7. ({@code COMPLETED},       {@code COMPLETED})        >> {@code COMPLETED}  all done
 * 8. ({@code COMPLETED},       {@code CANCELED})         >> NOT ALLOWED
 * </pre>
 *
 * <p>The ({@code COMPLETED}, {@code NON_COMPLETED}) cases should be figured out and processed again
 * if needed, because these cases mean that the first step was already done, and they have
 * discrepancies with other steps. (e.g., PG payment done, but point payment failed.)
 *
 * <p>Anything else - no transaction at all, or a current step still INIT / READY - means the
 * payment is simply still running, so it stays {@link PaymentStatus#PROCESSING}.
 *
 * <p>Constraints: After the tx which is not COMPLETED, all the following txs must be INIT status.
 */
public class PaymentStatusRuleResolver {
  private static final int FIRST_STEP_INDEX = 0;
  private static final int ALL_COMPLETED_STEP = -1;

  /**
   * @param transactions the payment's transactions, sorted by sequence order
   * @return the status the owning Payment should hold
   */
  public static PaymentStatus resolve(List<PaymentTransaction> transactions) {
    // not allowed for empty transaction list
    if (transactions.isEmpty()) {
      throw new IllegalArgumentException("Ordered transactions must not be empty");
    }

    // find the index of the last non-completed transaction
    int currentNonCompletedIndex = indexOfCurrentNonCompletedStep(transactions);

    // all the transactions completed (Case 7)
    if (currentNonCompletedIndex == ALL_COMPLETED_STEP) {
      return PaymentStatus.COMPLETED;
    }

    // an earlier step already succeeded, so the payment needs settlement, retry or either way.
    if (currentNonCompletedIndex > FIRST_STEP_INDEX) {
      checkIfSubsequentTransactionsAreAllInit(transactions, currentNonCompletedIndex);

      return PaymentStatus.PENDING;
    }

    // the first step has not been done yet - the first step alone decides.
    return switch (transactions.get(currentNonCompletedIndex).getStatus()) {
      case INIT, READY -> PaymentStatus.PROCESSING;
      case PENDING, ACTION_REQUIRED -> PaymentStatus.PENDING;
      case FAILED -> PaymentStatus.FAILED;
      case CANCELED -> PaymentStatus.CANCELED;
      case COMPLETED -> throw new IllegalStateException("The current step must not be COMPLETED");
    };
  }

  /**
   * @return the index of the first transaction that is not COMPLETED, or {@link
   *     #ALL_COMPLETED_STEP} when every transaction is COMPLETED
   */
  private static int indexOfCurrentNonCompletedStep(List<PaymentTransaction> transactions) {
    return IntStream.range(FIRST_STEP_INDEX, transactions.size())
        .filter(index -> transactions.get(index).isNotCompleted())
        .findFirst()
        .orElse(ALL_COMPLETED_STEP);
  }

  private static void checkIfSubsequentTransactionsAreAllInit(
      List<PaymentTransaction> transactions, int index) {
    if (transactions.subList(index + 1, transactions.size()).stream()
        .allMatch(PaymentTransaction::isInit)) {
      return;
    }

    throw new IllegalStateException(
        "After the tx which is not COMPLETED, all the following txs must be INIT status (payment ID: %d)"
            .formatted(transactions.getFirst().getPaymentId()));
  }
}
