package dev.teamuts.payment.domain.pg.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PgProviderType {
  NONE("none"),
  TEST("local-test"),
  STRIPE("stripe"),
  ;

  private final String pgName;

  private static final Map<String, PgProviderType> PG_NAME_MAP =
      Arrays.stream(PgProviderType.values())
          .collect(Collectors.toUnmodifiableMap(PgProviderType::getPgName, e -> e));

  public static PgProviderType fromPGName(String pgName) {
    return PG_NAME_MAP.getOrDefault(pgName, NONE);
  }
}
