package dev.teamuts.payment.domain.payment.constant;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaySequenceType {
  PG_PAY(1, Payment::hasPayAmount, PaymentTransaction::initPay),
  POINT(2, Payment::hasPointAmount, PaymentTransaction::initPoint);

  private final Integer seqNumber;

  @Getter(AccessLevel.NONE)
  private final Predicate<Payment> checkApplicable;

  @Getter(AccessLevel.NONE)
  private final Function<Payment, PaymentTransaction> initializer;

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

  public boolean isApplicableTo(Payment payment) {
    return checkApplicable.test(payment);
  }

  public PaymentTransaction initTransaction(Payment payment) {
    return initializer.apply(payment);
  }
}
