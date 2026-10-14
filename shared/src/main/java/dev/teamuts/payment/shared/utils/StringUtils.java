package dev.teamuts.payment.shared.utils;

public class StringUtils {
  public static String truncate(String str, int maxLength) {
    if (str == null) {
      return null;
    }

    String target = str.trim();

    if (target.isEmpty()) {
      return target;
    }

    if (target.length() <= maxLength) {
      return target;
    }

    return target.substring(0, maxLength);
  }
}
