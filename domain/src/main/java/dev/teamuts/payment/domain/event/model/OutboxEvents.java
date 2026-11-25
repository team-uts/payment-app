package dev.teamuts.payment.domain.event.model;

import dev.teamuts.shared.domain.model.BaseIdentifiableModelCollection;
import java.util.List;

public class OutboxEvents extends BaseIdentifiableModelCollection<Long, OutboxEvent> {
  protected OutboxEvents(List<OutboxEvent> models) {
    super(models);
  }

  public static OutboxEvents of(List<OutboxEvent> models) {
    return new OutboxEvents(models);
  }
}
