package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.shared.domain.model.BaseIdentifiableModelCollection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Payments extends BaseIdentifiableModelCollection<Long, Payment> {
  protected Payments(List<Payment> payments) {
    super(payments);
  }

  public static Payments empty() {
    return new Payments(Collections.emptyList());
  }

  public static Payments of(List<Payment> payments) {
    if (payments == null || payments.isEmpty()) {
      throw new IllegalArgumentException("payments cannot be null or empty");
    }

    return new Payments(Collections.unmodifiableList(payments));
  }

  public void processEachPayment(Consumer<Payment> paymentConsumer) {
    for (Payment payment : elements()) {
      paymentConsumer.accept(payment);

      if (payment.isNotCompleted()) {
        break;
      }
    }
  }

  public OrderStatus resolveOrderStatus() {
    List<Payment> sortedPayments =
        elements().stream()
            .sorted(Comparator.comparingInt(payment -> payment.getStatus().getDecideOrder()))
            .toList();

    int firstNotCompletedIndex =
        IntStream.range(0, sortedPayments.size())
            .filter(index -> sortedPayments.get(index).isNotCompleted())
            .findFirst()
            .orElse(-1);

    if (firstNotCompletedIndex == -1) {
      return OrderStatus.CONFIRMED;
    }

    if (firstNotCompletedIndex > 0) {
      return OrderStatus.PAYMENT_PENDING;
    }

    return switch (sortedPayments.get(firstNotCompletedIndex).getStatus()) {
      case PaymentStatus.PROCESSING, PaymentStatus.PENDING -> OrderStatus.PAYMENT_PENDING;
      case PaymentStatus.FAILED -> OrderStatus.FAILED;
      case PaymentStatus.CANCELED -> OrderStatus.CANCELED;
      case PaymentStatus.COMPLETED ->
          throw new IllegalStateException("The current step must not be COMPLETED");
    };
  }
}
