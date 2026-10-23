package dev.teamuts.payment.domain.payment;

import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class MoneyTestFactory {
  /** Step between two adjacent amounts when none is given: the currency's smallest minor unit. */
  private static final long DEFAULT_STEP_MINOR_UNITS = 1L;

  /**
   * Creates Money with a random amount within [min, max], where min and max are identifying the
   * boundary and step is configuring the interval. A step of 1 means every representable amount is
   * reachable (0.01 for USD, 1 for KRW); a step of 50 on USD yields only amounts ending in .00 or
   * .50. The amount is rounded to the currency's scale.
   */
  public static Money createSingle(long min, long max, long stepUnit, Currency currency) {
    if (min < 0) {
      throw new IllegalArgumentException("min must be greater than or equal to zero");
    }
    if (min > max) {
      throw new IllegalArgumentException("min must be less than or equal to max");
    }
    if (stepUnit <= 0) {
      throw new IllegalArgumentException("step must be greater than zero");
    }

    // e.g., min=40, max=220, step 50 => {50, 100, 150, 200}
    // minStep=1, maxStep=4
    long minStep = min % stepUnit == 0 ? min / stepUnit : (min / stepUnit + 1);
    long maxStep = max / stepUnit;
    long finalValue = ThreadLocalRandom.current().nextLong(minStep, maxStep + 1) * stepUnit;

    return Money.of(BigDecimal.valueOf(finalValue, currency.getScale()), currency);
  }

  /** Creates Money with a random amount inside the interval configured on the TestCurrency. */
  public static Money createSingle(Currency currency) {
    TestCurrency testCurrency = TestCurrency.fromCurrency(currency);

    return createSingle(
        testCurrency.getMin(),
        testCurrency.getMax(),
        testCurrency.getStepUnits(),
        testCurrency.getCurrency());
  }

  /** Each constant configures its own amount interval: lower bound, upper bound and step. */
  @Getter
  @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
  public enum TestCurrency {
    USD(Currency.USD, 100L, 10_000L, 50L),
    AUD(Currency.AUD, 100L, 10_000L, 50L),
    KRW(Currency.KRW, 1_000L, 10_000_000L, 50L);

    private final Currency currency;
    private final long min;
    private final long max;
    private final long stepUnits;

    private static final Map<Currency, TestCurrency> currencyMap =
        Arrays.stream(TestCurrency.values())
            .collect(Collectors.toUnmodifiableMap(TestCurrency::getCurrency, e -> e));

    static TestCurrency fromCurrency(Currency currency) {
      TestCurrency testCurrency = currencyMap.get(currency);

      if (testCurrency == null) {
        throw new IllegalArgumentException("No TestCurrency found for currency: " + currency);
      }

      return testCurrency;
    }
  }
}
