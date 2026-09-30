package dev.teamuts.payment.domain.pg.constant;

import java.util.Arrays;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PGProviderType {
  NONE("none"),
  STRIPE("stripe"),
  ;

  private final String pgName;

  private static final Map<String, PGProviderType> PG_NAME_MAP =
      Arrays.stream(PGProviderType.values())
          .collect(
              java.util.stream.Collectors.toUnmodifiableMap(PGProviderType::getPgName, e -> e));

  public static PGProviderType fromPGName(String pgName) {
    return PG_NAME_MAP.getOrDefault(pgName, NONE);
  }
}
