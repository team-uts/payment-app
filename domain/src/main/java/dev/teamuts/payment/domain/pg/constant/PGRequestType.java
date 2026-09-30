package dev.teamuts.payment.domain.pg.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PGRequestType {
  NONE("none"),
  ACCOUNT("account"),
  PAYMENT_METHOD_SETUP("payment-method-setup"),
  PAYMENT_METHOD("payment-method"),
  PAYMENT("payment");

  private final String pathName;

  private static final Map<String, PGRequestType> PATH_NAME_MAP =
      Arrays.stream(values())
          .collect(Collectors.toUnmodifiableMap(PGRequestType::getPathName, e -> e));

  public static PGRequestType fromPathName(String pathName) {
    return PATH_NAME_MAP.getOrDefault(pathName, NONE);
  }
}
