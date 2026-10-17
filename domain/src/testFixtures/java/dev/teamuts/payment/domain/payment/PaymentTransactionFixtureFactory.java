package dev.teamuts.payment.domain.payment;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

public class PaymentTransactionFixtureFactory {
  public PaymentTransaction createSingleRandom(
      PaymentTransactionStatus status, PaySequenceType sequenceType) {
    EasyRandomParameters params =
        new EasyRandomParameters()
            .randomize(
                FieldPredicates.named("id")
                    .or(FieldPredicates.named("memberId"))
                    .or(FieldPredicates.named("paymentId"))
                    .or(FieldPredicates.named("orderId"))
                    .or(FieldPredicates.named("payMethodId")),
                () -> ThreadLocalRandom.current().nextLong(1L, 10_000L))
            .randomize(
                FieldPredicates.named("payMethodType"),
                () ->
                    PaymentMethodType.values()[
                        new Random().nextInt(PaymentMethodType.values().length)])
            .randomize(
                FieldPredicates.named("serviceType"),
                () -> AppServiceType.values()[new Random().nextInt(AppServiceType.values().length)])
            .randomize(
                FieldPredicates.named("amount"), () -> MoneyTestFactory.createSingle(Currency.AUD))
            .randomize(FieldPredicates.named("currency"), () -> Currency.AUD)
            .randomize(
                FieldPredicates.named("pgProvider"),
                () -> PGProviderType.values()[new Random().nextInt(PGProviderType.values().length)])
            .randomize(
                FieldPredicates.named("status"),
                () ->
                    status == null
                        ? PaymentTransactionStatus.values()[
                            new Random().nextInt(PaymentTransactionStatus.values().length)]
                        : status)
            .randomize(
                FieldPredicates.named("sequenceType"),
                () ->
                    sequenceType == null
                        ? PaySequenceType.values()[
                            new Random().nextInt(PaySequenceType.values().length)]
                        : sequenceType);

    return new EasyRandom(params).nextObject(PaymentTransaction.class);
  }
}
