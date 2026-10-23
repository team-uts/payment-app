package dev.teamuts.payment.domain.payment.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.*;

import dev.teamuts.payment.domain.payment.PaymentTransactionFixtureFactory;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PaymentStatusRuleResolverTest {
  private PaymentTransactionFixtureFactory paymentTransactionFactory;

  static Stream<Arguments> provideTwoTxsForResolvingPaymentStatusTest() {
    return Stream.of(
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.PENDING),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.ACTION_REQUIRED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.INIT),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT)),
            PaymentStatus.PROCESSING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.READY),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT)),
            PaymentStatus.PROCESSING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.CANCELED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT)),
            PaymentStatus.CANCELED),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.READY)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.PENDING)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.ACTION_REQUIRED)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.FAILED)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.COMPLETED)),
            PaymentStatus.COMPLETED));
  }

  static Stream<Arguments> provideOverThreeTxsForResolvingPaymentStatusTest() {
    return Stream.of(
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.PENDING),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.PENDING),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.INIT)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.COMPLETED),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.COMPLETED)),
            PaymentStatus.COMPLETED));
  }

  static Stream<Arguments> provideTwoInvalidTxs() {
    return Stream.of(
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.PENDING),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.PENDING)),
            PaymentStatus.PENDING),
        arguments(
            List.of(
                Map.entry(PaySequenceType.PG_PAY, PaymentTransactionStatus.PENDING),
                Map.entry(PaySequenceType.POINT, PaymentTransactionStatus.COMPLETED)),
            PaymentStatus.PENDING));
  }

  @BeforeEach
  void setup() {
    paymentTransactionFactory = new PaymentTransactionFixtureFactory();
  }

  @ParameterizedTest
  @MethodSource({
    "provideTwoTxsForResolvingPaymentStatusTest",
    "provideOverThreeTxsForResolvingPaymentStatusTest"
  })
  void testMultipleTxsReturnValidPaymentStatus(
      List<Map.Entry<PaySequenceType, PaymentTransactionStatus>> testCases,
      PaymentStatus expectedStatus) {

    // Given a list of test cases and an expected payment status
    List<PaymentTransaction> txs =
        testCases.stream()
            .map(e -> paymentTransactionFactory.createSingleRandom(e.getValue(), e.getKey()))
            .toList();

    PaymentStatus resultStatus = PaymentStatusRuleResolver.resolve(txs);

    assertThat(resultStatus).isEqualTo(expectedStatus);
  }

  @ParameterizedTest
  @MethodSource({"provideTwoInvalidTxs"})
  void throwExceptionInvalidMultipleTxStatuses(
      List<Map.Entry<PaySequenceType, PaymentTransactionStatus>> testCases,
      PaymentStatus expectedStatus) {

    // Given a list of test cases and an expected payment status
    List<PaymentTransaction> txs =
        testCases.stream()
            .map(e -> paymentTransactionFactory.createSingleRandom(e.getValue(), e.getKey()))
            .toList();

    assertThatThrownBy(() -> PaymentStatusRuleResolver.resolve(txs))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(
            "After the tx which is not COMPLETED, all the following txs must be INIT status");
  }
}
