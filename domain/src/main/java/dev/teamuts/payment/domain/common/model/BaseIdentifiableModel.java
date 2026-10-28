package dev.teamuts.payment.domain.common.model;

import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class BaseIdentifiableModel<ID> {
  protected ID id;

  public final boolean isPersisted() {
    return switch (id) {
      case Number numId -> numId.longValue() > 0;
      case UUID uuid -> !uuid.equals(new UUID(0L, 0L));
      case null, default -> false;
    };
  }
}
