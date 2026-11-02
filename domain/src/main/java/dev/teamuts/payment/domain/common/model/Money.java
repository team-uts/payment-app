package dev.teamuts.payment.domain.common.model;

import dev.teamuts.payment.domain.common.exception.MoneyErrorCode;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.shared.error.exception.BusinessException;
import dev.teamuts.payment.shared.error.exception.ErrorArguments;
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
      throw BusinessException.of(MoneyErrorCode.LESS_THAN_ZERO, ErrorArguments.of(amount));
    }

    return new Money(amount.setScale(currency.getScale(), RoundingMode.UNNECESSARY), currency);
  }

  private void checkIfSameCurrency(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw BusinessException.of(
          MoneyErrorCode.CURRENCY_MISMATCH, ErrorArguments.of(this.currency, other.currency));
    }
  }

  public boolean isEqual(Money other) {
    checkIfSameCurrency(other);

    return MoneyCalculator.isEqual(this.amount, other.amount);
  }

  public boolean isGreaterThanZero() {
    return MoneyCalculator.isGreaterThan(
        this.amount, BigDecimal.ZERO.setScale(currency.getScale(), RoundingMode.UNNECESSARY));
  }

  public boolean isZero() {
    return MoneyCalculator.isEqual(
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

  public Money copy() {
    return Money.of(this.amount, this.currency);
  }

  public boolean hasSameCurrency(Money other) {
    return this.currency.equals(other.currency);
  }

  public boolean hasNotSameCurrency(Money other) {
    return !hasSameCurrency(other);
  }
}
