package dev.teamuts.payment.domain.common.model;

import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.shared.utils.MoneyCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Money {
  @EqualsAndHashCode.Include(rank = 1)
  private BigDecimal amount;

  @EqualsAndHashCode.Include(rank = 2)
  private Currency currency;

  public static Money zero(Currency currency) {
    return of(BigDecimal.ZERO, currency);
  }

  public static Money of(BigDecimal amount, Currency currency) {
    if (MoneyCalculator.isLessThanZero(amount)) {
      throw new RuntimeException("Amount should be greater than or equal to zero");
    }

    return new Money(amount.setScale(currency.getScale(), RoundingMode.UNNECESSARY), currency);
  }

  private void checkIfSameCurrency(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new RuntimeException("Currencies must be the same");
    }
  }

  public boolean isEqual(Money other) {
    checkIfSameCurrency(other);

    return MoneyCalculator.isEqual(this.amount, other.amount);
  }

  public boolean isGreaterThan(Money other) {
    checkIfSameCurrency(other);

    return MoneyCalculator.isGreaterThan(this.amount, other.amount);
  }

  public boolean isGreaterThanZero() {
    return MoneyCalculator.isGreaterThan(
        this.amount, BigDecimal.ZERO.setScale(currency.getScale(), RoundingMode.UNNECESSARY));
  }

  public boolean isLessThan(Money other) {
    checkIfSameCurrency(other);

    return MoneyCalculator.isLessThan(this.amount, other.amount);
  }

  public Money minus(Money other) {
    checkIfSameCurrency(other);

    return Money.of(MoneyCalculator.subtract(this.amount, other.amount), this.currency);
  }
}
