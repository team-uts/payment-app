package dev.teamuts.payment.domain.payment.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaySequenceType {
  PG_PAY(1),
  POINT(2);

  private final Integer seqNumber;

  private static final Map<Integer, PaySequenceType> SEQ_TYPE_MAPMAP =
      Arrays.stream(PaySequenceType.values())
          .collect(Collectors.toUnmodifiableMap(PaySequenceType::getSeqNumber, e -> e));

  public static PaySequenceType of(Integer seqNumber) {
    PaySequenceType seqType = SEQ_TYPE_MAPMAP.get(seqNumber);

    if (seqType == null) {
      throw new IllegalArgumentException("Invalid seqNumber: " + seqNumber);
    }

    return seqType;
  }
}
