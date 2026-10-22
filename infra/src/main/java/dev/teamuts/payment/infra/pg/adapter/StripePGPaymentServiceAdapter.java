package dev.teamuts.payment.infra.pg.adapter;

import static dev.teamuts.payment.infra.pg.utils.StripeOperationExecutor.executeStripeHandlingException;

import com.stripe.StripeClient;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.ExtPgPaymentTransaction;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPGPaymentServicePort;
import dev.teamuts.payment.infra.common.annotation.PGAdapter;
import dev.teamuts.payment.infra.pg.constant.StripeCurrency;
import dev.teamuts.payment.infra.pg.constant.StripeOperationType;
import dev.teamuts.payment.infra.pg.constant.StripePaymentTransactionStatus;
import dev.teamuts.payment.infra.pg.mapper.StripePayTransactionConverter;
import lombok.RequiredArgsConstructor;

@PGAdapter
@RequiredArgsConstructor
public class StripePGPaymentServiceAdapter implements ExternalPGPaymentServicePort {
  private static final PGProviderType PG_PROVIDER = PGProviderType.STRIPE;

  private final StripeClient stripeClient;
  private final StripePayTransactionConverter stripePayTransactionConverter;

  @Override
  public boolean supports(PGProviderType key) {
    return key == PG_PROVIDER;
  }

  @Override
  public ExtPgPaymentTransaction retrievePGPaymentTransaction(PaymentTransaction transaction) {
    return executeStripeHandlingException(
        StripeOperationType.RETRIEVE_PAYMENT_INTENT,
        operationType -> {
          // retrieve PG tx by pgTransactionId
          PaymentIntent paymentIntent =
              stripeClient.v1().paymentIntents().retrieve(transaction.getPgTransactionId());

          return stripePayTransactionConverter.fromPaymentIntent(
              paymentIntent, transaction, operationType);
        },
        (stripeE, operationType) ->
            stripePayTransactionConverter.fromStripeException(stripeE, operationType, transaction),
        (e, operationType) ->
            stripePayTransactionConverter.fromException(e, operationType, transaction));
  }

  /**
   * @return Usually PaymentTransaction status would be {@link
   *     StripePaymentTransactionStatus#REQUIRES_CONFIRMATION} in most cases. It could be {@link
   *     StripePaymentTransactionStatus#REQUIRES_PAYMENT_METHOD}
   */
  @Override
  public ExtPgPaymentTransaction createPGTransaction(
      PaymentTransaction transaction, PGAccount pgAccount, PaymentMethod paymentMethod) {
    return executeStripeHandlingException(
        StripeOperationType.CREATE_PAYMENT_INTENT,
        operationType -> {
          StripeCurrency currency = StripeCurrency.of(transaction.getCurrency());

          // return_url (setAllowRedirects) off
          PaymentIntentCreateParams params =
              PaymentIntentCreateParams.builder()
                  .setAmount(currency.getTransactionAmount(transaction.getAmount()))
                  .setCurrency(currency.getCurrencyCode())
                  .setCustomer(pgAccount.getPgAccountId())
                  .setPaymentMethod(paymentMethod.getProviderToken())
                  .setAutomaticPaymentMethods(
                      PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                          .setEnabled(true)
                          .setAllowRedirects(AllowRedirects.NEVER)
                          .build())
                  .build();

          PaymentIntent paymentIntent = stripeClient.v1().paymentIntents().create(params);

          return stripePayTransactionConverter.fromPaymentIntent(
              paymentIntent, transaction, operationType);
        },
        (stripeE, operationType) ->
            stripePayTransactionConverter.fromStripeException(stripeE, operationType, transaction),
        (e, operationType) ->
            stripePayTransactionConverter.fromException(e, operationType, transaction));
  }

  @Override
  public ExtPgPaymentTransaction confirmPayment(PaymentTransaction transaction) {
    return executeStripeHandlingException(
        StripeOperationType.CONFIRM_PAYMENT_INTENT,
        operationType -> {
          RequestOptions options =
              RequestOptions.builder()
                  .setReadTimeout(60_000) // 60 seconds
                  .setConnectTimeout(10_000) // 10 seconds
                  .build();

          PaymentIntent paymentIntent =
              stripeClient.v1().paymentIntents().confirm(transaction.getPgTransactionId(), options);

          return stripePayTransactionConverter.fromPaymentIntent(
              paymentIntent, transaction, operationType);
        },
        (stripeE, operationType) ->
            stripePayTransactionConverter.fromStripeExceptionWithTimeout(
                stripeE, operationType, transaction),
        (e, operationType) ->
            stripePayTransactionConverter.fromException(e, operationType, transaction));
  }
}
