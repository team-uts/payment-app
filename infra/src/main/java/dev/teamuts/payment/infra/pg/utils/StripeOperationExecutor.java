package dev.teamuts.payment.infra.pg.utils;

import com.stripe.exception.StripeException;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.infra.pg.constant.StripeOperationType;
import java.util.function.BiFunction;
import lombok.extern.slf4j.Slf4j;

// TODO: handle other exceptions
@Slf4j
public class StripeOperationExecutor {
  private static final PGProviderType PG_PROVIDER = PGProviderType.STRIPE;

  public static <T> T executeStripeHandlingException(
      StripeOperationType operationType,
      StripeOperation<T> operation,
      BiFunction<StripeException, StripeOperationType, T> stripeExceptionHandler,
      BiFunction<Exception, StripeOperationType, T> exceptionHandler) {
    try {
      return operation.execute(operationType);
    } catch (StripeException e) {
      log.error(
          "[{}] Status: {}, Code: {}, Msg: {}",
          PG_PROVIDER,
          e.getStatusCode(),
          e.getCode(),
          e.getMessage());

      // Handle StripeException or other exceptions here if needed
      if (stripeExceptionHandler != null) {
        return stripeExceptionHandler.apply(e, operationType);
      }

      throw new RuntimeException("[%s] %s - failed".formatted(PG_PROVIDER, operationType.name()));
    } catch (Exception e) {
      log.error(e.getMessage());

      if (exceptionHandler != null) {
        return exceptionHandler.apply(e, operationType);
      }

      throw new RuntimeException("[%s] %s - failed".formatted(PG_PROVIDER, operationType.name()));
    }
  }

  public static <T> T executeStripe(
      StripeOperationType operationType, StripeOperation<T> operation) {
    return executeStripeHandlingException(operationType, operation, null, null);
  }
}
