package dev.teamuts.payment.infra.pg.constant;

import dev.teamuts.payment.domain.payment.constant.Currency;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StripeCurrency {
  USD("usd", Currency.USD, 2, 50),
  AUD("aud", Currency.AUD, 2, 50),
  KRW("krw", Currency.KRW, 0, 50);

  private final String currencyCode;
  private final Currency currency;
  private final int decimalScale;
  private final int minAmount;

  private static final Map<Currency, StripeCurrency> CURRENCY_MAP =
      Arrays.stream(StripeCurrency.values())
          .collect(Collectors.toUnmodifiableMap(StripeCurrency::getCurrency, e -> e));

  public static StripeCurrency of(Currency currency) {
    StripeCurrency stripeCurrency = CURRENCY_MAP.getOrDefault(currency, null);

    if (stripeCurrency == null) {
      throw new IllegalArgumentException("Invalid Currency: %s".formatted(currency));
    }

    return stripeCurrency;
  }

  public Long getTransactionAmount(BigDecimal amount) {
    return amount.movePointRight(decimalScale).longValueExact();
  }
}
