package dev.teamuts.payment.domain.payment.facade.pay.helper;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PayProcessStepPipeline {
  private final PaymentTransaction transaction;
  private boolean stopped;

  public static PayProcessStepPipeline start(PaymentTransaction transaction) {
    return new PayProcessStepPipeline(transaction, false);
  }

  public PayProcessStepPipeline runIf(
      Predicate<PaymentTransaction> condition, Consumer<PaymentTransaction> action) {
    if (isNotStopped() && condition.test(transaction)) {
      action.accept(transaction);
    }

    return this;
  }

  public PayProcessStepPipeline stopWhen(Predicate<PaymentTransaction> condition) {
    if (isNotStopped() && condition.test(transaction)) {
      stopped = true;
    }

    return this;
  }

  public boolean isNotStopped() {
    return !stopped;
  }
}
