package dev.teamuts.payment.infra.pg.utils;

import dev.teamuts.payment.infra.pg.constant.StripeOperationType;

@FunctionalInterface
public interface StripeOperation<T> {
  T execute(StripeOperationType operationType) throws Exception;
}
