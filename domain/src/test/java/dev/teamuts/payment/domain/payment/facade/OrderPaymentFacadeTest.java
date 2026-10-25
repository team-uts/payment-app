package dev.teamuts.payment.domain.payment.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.OrderFixtureFactory;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.constant.Currency;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.factory.PaymentFixtureFactory;
import dev.teamuts.payment.domain.payment.factory.PaymentTransactionFixtureFactory;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderPaymentFacadeTest {
  private static final String EXT_ORDER_NUM = "EXT-ORDER-0001";
  private static final Currency CURRENCY = Currency.AUD;
  private static final BigDecimal TOTAL_AMOUNT = BigDecimal.valueOf(10_000);
  private static final BigDecimal POINT_AMOUNT = BigDecimal.valueOf(2_000);
  private static final Long PAY_METHOD_ID = 1_000L;

  @Mock private InitPaymentFacade initPaymentFacade;
  @Mock private PaymentTransactionExecutor paymentTransactionExecutor;
  @Mock private OrderService orderService;
  @Mock private PaymentTransactionService paymentTransactionService;
  @Mock private PaymentService paymentService;

  @InjectMocks private OrderPaymentFacade orderPaymentFacade;

  private PayOrderCommand command;

  @BeforeEach
  void setup() {
    command =
        new PayOrderCommand(
            EXT_ORDER_NUM,
            PAY_METHOD_ID,
            Money.of(TOTAL_AMOUNT, CURRENCY),
            Money.zero(CURRENCY),
            CURRENCY,
            AppServiceType.TAXI);
  }

  @Test
  @DisplayName("processes every step in sequence and returns the resolved payment")
  void processPaymentExecutesEachStepInOrder() {
    // Given an givenOrder which is ready for givenPayment and a givenPayment in PROCESSING status
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        PaymentFixtureFactory.createSingle(
            givenOrder.getId(),
            givenOrder.getMemberId(),
            PaymentStatus.PROCESSING,
            TOTAL_AMOUNT,
            BigDecimal.ZERO);
    PaymentTransaction givenTransaction =
        PaymentTransactionFixtureFactory.createSingleRandom(
            givenPayment.getMemberId(),
            givenPayment.getId(),
            givenPayment.getOrderId(),
            givenPayment.getPayMethodId(),
            PaymentTransactionStatus.INIT,
            PaySequenceType.PG_PAY);
    PaymentTransactions transactions = PaymentTransactions.of(List.of(givenTransaction));

    when(initPaymentFacade.getProcessingPaymentOrCreate(givenOrder, command))
        .thenReturn(givenPayment);
    when(paymentTransactionService.getTransactionsOrCreateInit(givenPayment))
        .thenReturn(transactions);
    doAnswer(
            invocation -> {
              PaymentTransaction argTransaction = invocation.getArgument(0);

              ReflectionTestUtils.setField(
                  argTransaction, "status", PaymentTransactionStatus.COMPLETED);

              return null;
            })
        .when(paymentTransactionExecutor)
        .executeProcessing(givenTransaction);

    doAnswer(
            invocation -> {
              Payment argPayment = invocation.getArgument(0);
              PaymentTransactions argTransactions = invocation.getArgument(1);

              ReflectionTestUtils.setField(
                  argPayment, "status", argTransactions.resolvePaymentStatus());

              return null;
            })
        .when(paymentService)
        .integrateTransactions(givenPayment, transactions);

    // When
    Payment result = orderPaymentFacade.processPayment(givenOrder, command);

    // Then
    assertThat(result).isSameAs(givenPayment);
    assertThat(result.getStatus()).isEqualTo(PaymentStatus.COMPLETED);

    // And every collaborator is called in the expected givenOrder
    InOrder inOrder =
        inOrder(
            initPaymentFacade,
            orderService,
            paymentTransactionService,
            paymentTransactionExecutor,
            paymentService);

    inOrder.verify(initPaymentFacade).getProcessingPaymentOrCreate(givenOrder, command);
    inOrder.verify(orderService).prepareForPayment(givenOrder);
    inOrder.verify(paymentTransactionService).getTransactionsOrCreateInit(givenPayment);
    inOrder.verify(paymentTransactionExecutor).executeProcessing(any(PaymentTransaction.class));
    inOrder.verify(paymentService).integrateTransactions(givenPayment, transactions);
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  @DisplayName("executes every transaction while the previous one is COMPLETED")
  void processPaymentExecutesEveryTransactionWhenPreviousOneIsCompleted() {
    // Given a givenPayment having both pay and point transactions, where the first one is COMPLETED
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        PaymentFixtureFactory.createSingle(
            givenOrder.getId(),
            givenOrder.getMemberId(),
            PaymentStatus.PROCESSING,
            TOTAL_AMOUNT,
            POINT_AMOUNT);

    PaymentTransaction givenPayTransaction =
        PaymentTransactionFixtureFactory.createSingleRandom(
            givenPayment.getMemberId(),
            givenPayment.getId(),
            givenPayment.getOrderId(),
            givenPayment.getPayMethodId(),
            PaymentTransactionStatus.COMPLETED,
            PaySequenceType.PG_PAY);

    PaymentTransaction givenPointTransaction =
        PaymentTransactionFixtureFactory.createSingleRandom(
            givenPayment.getMemberId(),
            givenPayment.getId(),
            givenPayment.getOrderId(),
            givenPayment.getPayMethodId(),
            PaymentTransactionStatus.COMPLETED,
            PaySequenceType.POINT);

    PaymentTransactions transactions =
        PaymentTransactions.of(List.of(givenPayTransaction, givenPointTransaction));

    when(initPaymentFacade.getProcessingPaymentOrCreate(givenOrder, command))
        .thenReturn(givenPayment);
    when(paymentTransactionService.getTransactionsOrCreateInit(givenPayment))
        .thenReturn(transactions);

    orderPaymentFacade.processPayment(givenOrder, command);

    // Then both transactions are executed in the pay sequence givenOrder
    InOrder inOrder = inOrder(paymentTransactionExecutor);
    inOrder.verify(paymentTransactionExecutor).executeProcessing(givenPayTransaction);
    inOrder.verify(paymentTransactionExecutor).executeProcessing(givenPointTransaction);

    verify(paymentService).integrateTransactions(givenPayment, transactions);
  }

  @Test
  @DisplayName("stops executing the following transactions once one is not COMPLETED")
  void processPaymentStopsAfterNotCompletedTransaction() {
    // Given a givenPayment whose first transaction (PG_PAY) ends up FAILED
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        PaymentFixtureFactory.createSingle(
            givenOrder.getId(),
            givenOrder.getMemberId(),
            PaymentStatus.PROCESSING,
            TOTAL_AMOUNT,
            POINT_AMOUNT);

    PaymentTransaction givenPayTransaction =
        PaymentTransactionFixtureFactory.createSingleRandom(
            givenPayment.getMemberId(),
            givenPayment.getId(),
            givenPayment.getOrderId(),
            givenPayment.getPayMethodId(),
            PaymentTransactionStatus.FAILED,
            PaySequenceType.PG_PAY);

    PaymentTransaction givenPointTransaction =
        PaymentTransactionFixtureFactory.createSingleRandom(
            givenPayment.getMemberId(),
            givenPayment.getId(),
            givenPayment.getOrderId(),
            givenPayment.getPayMethodId(),
            PaymentTransactionStatus.INIT,
            PaySequenceType.POINT);

    PaymentTransactions transactions =
        PaymentTransactions.of(List.of(givenPayTransaction, givenPointTransaction));

    when(initPaymentFacade.getProcessingPaymentOrCreate(givenOrder, command))
        .thenReturn(givenPayment);
    when(paymentTransactionService.getTransactionsOrCreateInit(givenPayment))
        .thenReturn(transactions);

    orderPaymentFacade.processPayment(givenOrder, command);

    // Then the following POINT transaction is never executed
    verify(paymentTransactionExecutor).executeProcessing(givenPayTransaction);
    verify(paymentTransactionExecutor, never()).executeProcessing(givenPointTransaction);

    // And the result is still integrated into the givenPayment
    verify(paymentService).integrateTransactions(givenPayment, transactions);
  }

  @Test
  @DisplayName("processes the payment which has only point amount")
  void processPaymentWithOnlyPointAmount() {
    // Given a givenPayment without pay amount but with point amount
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.PAYMENT_PENDING);
    Payment givenPayment =
        PaymentFixtureFactory.createSingle(
            givenOrder.getId(),
            givenOrder.getMemberId(),
            PaymentStatus.PROCESSING,
            BigDecimal.ZERO,
            POINT_AMOUNT);
    PaymentTransaction pointTransaction =
        PaymentTransactionFixtureFactory.createSingleRandom(
            givenPayment.getMemberId(),
            givenPayment.getId(),
            givenPayment.getOrderId(),
            givenPayment.getPayMethodId(),
            PaymentTransactionStatus.COMPLETED,
            PaySequenceType.POINT);
    PaymentTransactions transactions = PaymentTransactions.of(List.of(pointTransaction));

    when(initPaymentFacade.getProcessingPaymentOrCreate(givenOrder, command))
        .thenReturn(givenPayment);
    when(paymentTransactionService.getTransactionsOrCreateInit(givenPayment))
        .thenReturn(transactions);

    Payment result = orderPaymentFacade.processPayment(givenOrder, command);

    assertThat(result).isSameAs(givenPayment);
    verify(paymentTransactionExecutor).executeProcessing(pointTransaction);
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"CONFIRMED", "CANCELED", "FAILED"})
  @DisplayName("throws when the order is already in a final status")
  void processPaymentThrowsWhenOrderIsFinal(OrderStatus finalStatus) {
    // Given an order which is already closed
    Order order = OrderFixtureFactory.createSingle(finalStatus);

    assertThatThrownBy(() -> orderPaymentFacade.processPayment(order, command))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining(EXT_ORDER_NUM)
        .hasMessageContaining(finalStatus.name());

    // Then nothing is processed at all
    verifyNoInteractions(
        initPaymentFacade,
        orderService,
        paymentTransactionService,
        paymentTransactionExecutor,
        paymentService);
  }

  @ParameterizedTest
  @EnumSource(
      value = PaymentStatus.class,
      names = {"COMPLETED", "FAILED", "CANCELED"})
  @DisplayName("throws when the retrieved payment is already in a final status")
  void processPaymentThrowsWhenPaymentIsFinal(PaymentStatus finalStatus) {
    // Given a givenPayment which has already been closed
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.PAYMENT_PENDING);
    Payment givenPayment =
        PaymentFixtureFactory.createSingle(
            givenOrder.getId(),
            givenOrder.getMemberId(),
            finalStatus,
            TOTAL_AMOUNT,
            BigDecimal.ZERO);

    when(initPaymentFacade.getProcessingPaymentOrCreate(givenOrder, command))
        .thenReturn(givenPayment);

    assertThatThrownBy(() -> orderPaymentFacade.processPayment(givenOrder, command))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Cannot process payment" + givenPayment.getId());

    // Then the givenOrder is never prepared and no transaction is processed
    verifyNoInteractions(orderService, paymentTransactionService, paymentTransactionExecutor);
  }

  @Test
  @DisplayName("throws when the payment has neither pay amount nor point amount")
  void processPaymentThrowsWhenPaymentHasNoAmount() {
    // Given a PROCESSING givenPayment with zero amounts
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        PaymentFixtureFactory.createSingle(
            givenOrder.getId(),
            givenOrder.getMemberId(),
            PaymentStatus.PROCESSING,
            BigDecimal.ZERO,
            BigDecimal.ZERO);

    when(initPaymentFacade.getProcessingPaymentOrCreate(givenOrder, command))
        .thenReturn(givenPayment);

    assertThatThrownBy(() -> orderPaymentFacade.processPayment(givenOrder, command))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Cannot process payment" + givenPayment.getId());

    verifyNoInteractions(orderService, paymentTransactionService, paymentTransactionExecutor);
  }

  @Test
  @DisplayName("does not process any transaction when preparing the order fails")
  void processPaymentStopsWhenPrepareForPaymentFails() {
    // Given an givenOrder which cannot be changed into PAYMENT_PENDING
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        PaymentFixtureFactory.createSingle(
            givenOrder.getId(),
            givenOrder.getMemberId(),
            PaymentStatus.PROCESSING,
            TOTAL_AMOUNT,
            BigDecimal.ZERO);

    when(initPaymentFacade.getProcessingPaymentOrCreate(givenOrder, command))
        .thenReturn(givenPayment);
    doThrow(new RuntimeException("givenOrder status conflict"))
        .when(orderService)
        .prepareForPayment(givenOrder);

    assertThatThrownBy(() -> orderPaymentFacade.processPayment(givenOrder, command))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("givenOrder status conflict");

    verifyNoInteractions(paymentTransactionService, paymentTransactionExecutor, paymentService);
  }
}
