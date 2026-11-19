package dev.teamuts.payment.domain.common.lock;

import dev.teamuts.shared.data.constant.LockGroup;
import dev.teamuts.shared.data.constant.LockTtl;
import java.util.EnumSet;
import java.util.Locale;
import org.jspecify.annotations.NonNull;

public enum PaymentLockGroup implements LockGroup {
  PAY_ORDER(LockTtl.ONE_MINUTE),
  SETUP_PAYMENT_METHOD(LockTtl.ONE_MINUTE),
  ;

  private final LockTtl ttl;

  PaymentLockGroup() {
    this.ttl = LockTtl.FIVE_SECONDS;
  }

  PaymentLockGroup(LockTtl ttl) {
    this.ttl = ttl;
  }

  public static final String PAY_ORDER_NAME = "PAY_ORDER";
  public static final String SETUP_PAYMENT_METHOD_NAME = "SETUP_PAYMENT_METHOD";

  private static final EnumSet<PaymentLockGroup> ALL_GROUPS = EnumSet.allOf(PaymentLockGroup.class);

  public static PaymentLockGroup of(String group) {
    return ALL_GROUPS.stream()
        .filter(g -> g.name().toLowerCase(Locale.ROOT).equals(group.toLowerCase(Locale.ROOT)))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid lock group: " + group));
  }

  @Override
  public @NonNull LockTtl getLockTtl() {
    return this.ttl;
  }
}
