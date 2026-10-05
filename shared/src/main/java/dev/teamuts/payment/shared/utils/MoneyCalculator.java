package dev.teamuts.payment.shared.utils;

import java.math.BigDecimal;

public class MoneyCalculator {
  public static boolean isGreaterThan(BigDecimal amount1, BigDecimal amount2) {
    return amount1.compareTo(amount2) > 0;
  }

  public static boolean isLessThan(BigDecimal amount1, BigDecimal amount2) {
    return amount1.compareTo(amount2) < 0;
  }

  public static boolean isGreaterThanZero(BigDecimal amount) {
    return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
  }

  public static boolean isLessThanZero(BigDecimal amount) {
    return amount != null && amount.compareTo(BigDecimal.ZERO) < 0;
  }

  public static boolean isLessThanEqualToZero(BigDecimal amount) {
    return amount != null && amount.compareTo(BigDecimal.ZERO) <= 0;
  }

  public static boolean isEqual(BigDecimal amount1, BigDecimal amount2) {
    return amount1.compareTo(amount2) == 0;
  }

  public static boolean isNotEqual(BigDecimal amount1, BigDecimal amount2) {
    return !isEqual(amount1, amount2);
  }

  public static BigDecimal subtract(BigDecimal amount1, BigDecimal amount2) {
    return amount1.subtract(amount2);
  }
}
