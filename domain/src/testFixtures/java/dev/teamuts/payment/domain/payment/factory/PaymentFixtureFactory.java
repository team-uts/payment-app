package dev.teamuts.payment.domain.payment.factory;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import java.math.BigDecimal;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class PaymentFixtureFactory {
  private static final Long PAYMENT_ID = 10L;
  private static final Long PAY_METHOD_ID = 1_000L;
  private static final Currency CURRENCY = Currency.AUD;

  public static Payment createSingle(
      Long orderId,
      Long memberId,
      PaymentStatus status,
      BigDecimal payAmount,
      BigDecimal pointAmount) {
    return Payment.fromDatabase(
        PAYMENT_ID,
        orderId,
        memberId,
        AppServiceType.TAXI,
        status,
        PGProviderType.STRIPE,
        payAmount,
        pointAmount,
        CURRENCY,
        PAY_METHOD_ID,
        PaymentMethodType.CARD);
  }

  public static Payment createSingleRandom(
      PaymentStatus status, BigDecimal payAmount, BigDecimal pointAmount) {
    EasyRandomParameters params =
        new EasyRandomParameters()
            .randomize(field -> field.getName().equals("status"), () -> status)
            .randomize(
                field -> field.getName().equals("payAmount"),
                () -> MoneyFixtureFactory.createSingle(payAmount, Currency.AUD))
            .randomize(
                field -> field.getName().equals("pointAmount"),
                () -> MoneyFixtureFactory.createSingle(pointAmount, Currency.AUD))
            .randomize(field -> field.getName().equals("transactions"), () -> null);
    ;

    return new EasyRandom(params).nextObject(Payment.class);
  }
}
