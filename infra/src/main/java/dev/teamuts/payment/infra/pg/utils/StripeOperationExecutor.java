package dev.teamuts.payment.infra.pg.utils;

import com.stripe.exception.StripeException;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.infra.pg.constant.StripeOperationType;
import lombok.extern.slf4j.Slf4j;

// TODO: handle other exceptions
@Slf4j
public class StripeOperationExecutor {
  private static final PGProviderType PG_PROVIDER = PGProviderType.STRIPE;

  public static <T> T executeStripe(
      StripeOperationType operationType, StripeOperation<T> operation) {
    try {
      return operation.execute(operationType);
    } catch (StripeException e) {
      log.error(
          "[{}] Status: {}, Code: {}, Msg: {}",
          PG_PROVIDER,
          e.getStatusCode(),
          e.getCode(),
          e.getMessage());

      throw new RuntimeException("[%s] %s - failed".formatted(PG_PROVIDER, operationType.name()));
    } catch (Exception e) {
      log.error(e.getMessage());

      throw new RuntimeException("[%s] %s - failed".formatted(PG_PROVIDER, operationType.name()));
    }
  }
}
