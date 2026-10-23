package dev.teamuts.payment.infra.pg.mapper;

import com.stripe.exception.ApiConnectionException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeError;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.model.ExtPgPaymentTransaction;
import dev.teamuts.payment.infra.pg.constant.StripeOperationType;
import dev.teamuts.payment.infra.pg.constant.StripePaymentTransactionStatus;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class StripePayTransactionConverter {
  public ExtPgPaymentTransaction fromPaymentIntent(
      PaymentIntent paymentIntent,
      PaymentTransaction transaction,
      StripeOperationType operationType) {
    StripePaymentTransactionStatus pgTxStatus =
        StripePaymentTransactionStatus.of(paymentIntent.getStatus());

    Optional<StripeError> errorNullable = Optional.ofNullable(paymentIntent.getLastPaymentError());

    return ExtPgPaymentTransaction.fromPG(
        transaction,
        paymentIntent.getId(),
        pgTxStatus.name(),
        errorNullable.map(StripeError::getCode).orElse(null),
        errorNullable.map(StripeError::getMessage).orElse(null),
        operationType.name(),
        pgTxStatus.getDomainStatus());
  }

  public ExtPgPaymentTransaction fromStripeException(
      StripeException e, StripeOperationType operationType, PaymentTransaction transaction) {
    if (e instanceof InvalidRequestException) {
      return ExtPgPaymentTransaction.invalidRequest(
          transaction,
          String.valueOf(e.getStatusCode()),
          getErrorCodeFromException(e),
          e.getMessage(),
          operationType.name());
    }

    return ExtPgPaymentTransaction.apiFailed(
        transaction,
        String.valueOf(e.getStatusCode()),
        getErrorCodeFromException(e),
        e.getMessage(),
        operationType.name());
  }

  public ExtPgPaymentTransaction fromStripeExceptionWithTimeout(
      StripeException e, StripeOperationType operationType, PaymentTransaction transaction) {
    if (e instanceof ApiConnectionException) {
      return ExtPgPaymentTransaction.timeout(
          transaction,
          String.valueOf(e.getStatusCode()),
          getErrorCodeFromException(e),
          e.getMessage(),
          operationType.name());
    }

    return fromStripeException(e, operationType, transaction);
  }

  public ExtPgPaymentTransaction fromException(
      Exception e, StripeOperationType operationType, PaymentTransaction transaction) {
    return ExtPgPaymentTransaction.apiFailed(
        transaction, null, getErrorCodeFromException(e), e.getMessage(), operationType.name());
  }

  private String getErrorCodeFromException(Exception e) {
    if (e instanceof StripeException stripeE) {
      return stripeE.getCode() != null ? stripeE.getCode() : e.getClass().getSimpleName();
    }

    return e.getClass().getSimpleName();
  }
}
