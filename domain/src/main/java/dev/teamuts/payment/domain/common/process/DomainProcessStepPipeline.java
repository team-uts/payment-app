package dev.teamuts.payment.domain.common.process;

import dev.teamuts.payment.domain.common.model.BaseIdentifiableModel;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainProcessStepPipeline<T extends BaseIdentifiableModel<?>> {
  private final T model;
  private boolean stopped;

  public static <T extends BaseIdentifiableModel<?>> DomainProcessStepPipeline<T> start(T model) {
    return new DomainProcessStepPipeline<>(model, false);
  }

  /** only when the condition is satisfied, the action will be executed. */
  public DomainProcessStepPipeline<T> runIf(Predicate<T> condition, Consumer<T> action) {
    if (isNotStopped() && condition.test(model)) {
      action.accept(model);
    }

    return this;
  }

  /** if the condition is satisfied, the flag for stopping pipeline will be marked with true. */
  public DomainProcessStepPipeline<T> stopWhen(Predicate<T> condition) {
    if (isNotStopped() && condition.test(model)) {
      stopped = true;
    }

    return this;
  }

  public boolean isNotStopped() {
    return !stopped;
  }
}
