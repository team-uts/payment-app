package dev.teamuts.payment.domain.payment.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import dev.teamuts.payment.domain.event.constant.EventType;
import dev.teamuts.payment.domain.event.constant.OutboxEventStatus;
import dev.teamuts.payment.domain.event.handler.OutboxEventPublisher;
import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.order.OrderFixtureFactory;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.constant.OrderStatus;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.error.OrderErrorCode;
import dev.teamuts.payment.domain.order.error.OrderException;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.constant.PaymentStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.error.PaymentErrorCode;
import dev.teamuts.payment.domain.payment.error.PaymentException;
import dev.teamuts.payment.domain.payment.factory.PaymentFixtureFactory;
import dev.teamuts.payment.domain.payment.factory.PaymentTransactionFixtureFactory;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.payment.domain.payment.model.sub.PaymentRequests;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.payment.domain.payment.service.PaymentTransactionService;
import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.domain.model.Money;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
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
  @Mock private OutboxEventPublisher outboxEventPublisher;

  private OrderPaymentFacade orderPaymentFacade;
  private PaymentRequests paymentRequests;

  @BeforeEach
  void setup() {
    PaymentProcessor paymentProcessor =
        new PaymentProcessor(paymentTransactionExecutor, paymentTransactionService, paymentService);
    orderPaymentFacade =
        new OrderPaymentFacade(
            initPaymentFacade,
            paymentProcessor,
            orderService,
            paymentService,
            outboxEventPublisher);

    PayOrderCommand command =
        new PayOrderCommand(
            EXT_ORDER_NUM,
            AppServiceType.TAXI,
            PAY_METHOD_ID,
            Money.of(TOTAL_AMOUNT, CURRENCY),
            Money.zero(CURRENCY));

    paymentRequests = PaymentRequests.createSingleCustomerRequest(command);
  }

  @Test
  @DisplayName("processes every step in sequence and returns the processed payments")
  void processPaymentExecutesEachStepInOrder() {
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        paymentFor(givenOrder, PaymentStatus.PROCESSING, TOTAL_AMOUNT, BigDecimal.ZERO);
    PaymentTransaction givenTransaction =
        transactionFor(givenPayment, PaymentTransactionStatus.INIT, PaySequenceType.PG_PAY);
    PaymentTransactions transactions = PaymentTransactions.of(List.of(givenTransaction));
    Payments givenPayments = Payments.of(List.of(givenPayment));

    when(initPaymentFacade.getProcessingPaymentsOrCreate(givenOrder, paymentRequests))
        .thenReturn(givenPayments);
    when(paymentTransactionService.getTransactionsOrCreateInit(givenPayment))
        .thenReturn(transactions);
    doAnswer(
            invocation -> {
              ReflectionTestUtils.setField(
                  givenTransaction, "status", PaymentTransactionStatus.COMPLETED);

              return null;
            })
        .when(paymentTransactionExecutor)
        .executeProcessing(givenTransaction);

    doAnswer(
            invocation -> {
              givenPayment.integrateTransactions(transactions);

              return null;
            })
        .when(paymentService)
        .integrateTransactions(givenPayment, transactions);

    Payments result = orderPaymentFacade.processPayOrder(givenOrder, paymentRequests);

    assertThat(result).isSameAs(givenPayments);
    assertThat(givenPayment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);

    InOrder inOrder =
        inOrder(
            initPaymentFacade,
            orderService,
            paymentTransactionService,
            paymentTransactionExecutor,
            paymentService);

    inOrder.verify(initPaymentFacade).getProcessingPaymentsOrCreate(givenOrder, paymentRequests);
    inOrder.verify(orderService).prepareForPayment(givenOrder);
    inOrder.verify(paymentTransactionService).getTransactionsOrCreateInit(givenPayment);
    inOrder.verify(paymentTransactionExecutor).executeProcessing(givenTransaction);
    inOrder.verify(paymentService).integrateTransactions(givenPayment, transactions);
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  @DisplayName("executes every transaction while the previous one is COMPLETED")
  void processPaymentExecutesEveryTransactionWhenPreviousOneIsCompleted() {
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        paymentFor(givenOrder, PaymentStatus.PROCESSING, TOTAL_AMOUNT, POINT_AMOUNT);

    PaymentTransaction givenPayTransaction =
        transactionFor(givenPayment, PaymentTransactionStatus.COMPLETED, PaySequenceType.PG_PAY);

    PaymentTransaction givenPointTransaction =
        transactionFor(givenPayment, PaymentTransactionStatus.COMPLETED, PaySequenceType.POINT);

    PaymentTransactions transactions =
        PaymentTransactions.of(List.of(givenPayTransaction, givenPointTransaction));

    when(initPaymentFacade.getProcessingPaymentsOrCreate(givenOrder, paymentRequests))
        .thenReturn(Payments.of(List.of(givenPayment)));
    when(paymentTransactionService.getTransactionsOrCreateInit(givenPayment))
        .thenReturn(transactions);

    orderPaymentFacade.processPayOrder(givenOrder, paymentRequests);

    InOrder inOrder = inOrder(paymentTransactionExecutor);
    inOrder.verify(paymentTransactionExecutor).executeProcessing(givenPayTransaction);
    inOrder.verify(paymentTransactionExecutor).executeProcessing(givenPointTransaction);

    verify(paymentService).integrateTransactions(givenPayment, transactions);
  }

  @Test
  @DisplayName("stops executing the following transactions once one is not COMPLETED")
  void processPaymentStopsAfterNotCompletedTransaction() {
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        paymentFor(givenOrder, PaymentStatus.PROCESSING, TOTAL_AMOUNT, POINT_AMOUNT);

    PaymentTransaction givenPayTransaction =
        transactionFor(givenPayment, PaymentTransactionStatus.FAILED, PaySequenceType.PG_PAY);

    PaymentTransaction givenPointTransaction =
        transactionFor(givenPayment, PaymentTransactionStatus.INIT, PaySequenceType.POINT);

    PaymentTransactions transactions =
        PaymentTransactions.of(List.of(givenPayTransaction, givenPointTransaction));

    when(initPaymentFacade.getProcessingPaymentsOrCreate(givenOrder, paymentRequests))
        .thenReturn(Payments.of(List.of(givenPayment)));
    when(paymentTransactionService.getTransactionsOrCreateInit(givenPayment))
        .thenReturn(transactions);

    orderPaymentFacade.processPayOrder(givenOrder, paymentRequests);

    verify(paymentTransactionExecutor).executeProcessing(givenPayTransaction);
    verify(paymentTransactionExecutor, never()).executeProcessing(givenPointTransaction);

    verify(paymentService).integrateTransactions(givenPayment, transactions);
  }

  @Test
  @DisplayName("processes the payment which has only point amount")
  void processPaymentWithOnlyPointAmount() {
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.PAYMENT_PENDING);
    Payment givenPayment =
        paymentFor(givenOrder, PaymentStatus.PROCESSING, BigDecimal.ZERO, POINT_AMOUNT);
    PaymentTransaction pointTransaction =
        transactionFor(givenPayment, PaymentTransactionStatus.COMPLETED, PaySequenceType.POINT);
    PaymentTransactions transactions = PaymentTransactions.of(List.of(pointTransaction));
    Payments givenPayments = Payments.of(List.of(givenPayment));

    when(initPaymentFacade.getProcessingPaymentsOrCreate(givenOrder, paymentRequests))
        .thenReturn(givenPayments);
    when(paymentTransactionService.getTransactionsOrCreateInit(givenPayment))
        .thenReturn(transactions);

    Payments result = orderPaymentFacade.processPayOrder(givenOrder, paymentRequests);

    assertThat(result).isSameAs(givenPayments);
    verify(paymentTransactionExecutor).executeProcessing(pointTransaction);
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"CONFIRMED", "CANCELED", "FAILED"})
  @DisplayName("throws when the order is already in a final status")
  void processPaymentThrowsWhenOrderIsFinal(OrderStatus finalStatus) {
    Order order = OrderFixtureFactory.createSingle(finalStatus);

    assertThatThrownBy(() -> orderPaymentFacade.processPayOrder(order, paymentRequests))
        .isInstanceOf(OrderException.class)
        .hasMessageContaining(OrderErrorCode.CANNOT_PAY_PROCESS.name());

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
  @DisplayName("skips transaction processing for a finalized payment")
  void processPaymentSkipsFinalizedPayment(PaymentStatus finalStatus) {
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.PAYMENT_PENDING);
    Payment givenPayment = paymentFor(givenOrder, finalStatus, TOTAL_AMOUNT, BigDecimal.ZERO);

    Payments givenPayments = Payments.of(List.of(givenPayment));
    when(initPaymentFacade.getProcessingPaymentsOrCreate(givenOrder, paymentRequests))
        .thenReturn(givenPayments);

    Payments result = orderPaymentFacade.processPayOrder(givenOrder, paymentRequests);

    assertThat(result).isSameAs(givenPayments);
    assertThat(givenPayment.getStatus()).isEqualTo(finalStatus);
    verify(orderService).prepareForPayment(givenOrder);
    verifyNoInteractions(paymentTransactionService, paymentTransactionExecutor, paymentService);
  }

  @Test
  @DisplayName("throws when the payment has neither pay amount nor point amount")
  void processPaymentThrowsWhenPaymentHasNoAmount() {
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        paymentFor(givenOrder, PaymentStatus.PROCESSING, BigDecimal.ZERO, BigDecimal.ZERO);

    when(initPaymentFacade.getProcessingPaymentsOrCreate(givenOrder, paymentRequests))
        .thenReturn(Payments.of(List.of(givenPayment)));

    assertThatThrownBy(() -> orderPaymentFacade.processPayOrder(givenOrder, paymentRequests))
        .isInstanceOf(PaymentException.class)
        .hasMessageContaining(PaymentErrorCode.CANNOT_PROCESS_PAY_TX.name());

    verify(orderService).prepareForPayment(givenOrder);
    verifyNoInteractions(paymentTransactionService, paymentTransactionExecutor, paymentService);
  }

  @Test
  @DisplayName("does not process any transaction when preparing the order fails")
  void processPaymentStopsWhenPrepareForPaymentFails() {
    Order givenOrder = OrderFixtureFactory.createSingle(OrderStatus.INIT);
    Payment givenPayment =
        paymentFor(givenOrder, PaymentStatus.PROCESSING, TOTAL_AMOUNT, BigDecimal.ZERO);

    when(initPaymentFacade.getProcessingPaymentsOrCreate(givenOrder, paymentRequests))
        .thenReturn(Payments.of(List.of(givenPayment)));
    doThrow(new RuntimeException("givenOrder status conflict"))
        .when(orderService)
        .prepareForPayment(givenOrder);

    assertThatThrownBy(() -> orderPaymentFacade.processPayOrder(givenOrder, paymentRequests))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("givenOrder status conflict");

    verifyNoInteractions(paymentTransactionService, paymentTransactionExecutor, paymentService);
  }

  @ParameterizedTest
  @EnumSource(value = OrderStatus.class, names = "CONFIRMED", mode = EnumSource.Mode.EXCLUDE)
  @DisplayName("skips event publication for orders that are not confirmed")
  void skipsCompletedPaymentEventForUnconfirmedOrder(OrderStatus status) {
    Order order = OrderFixtureFactory.createSingle(status);

    orderPaymentFacade.publishCompletedPayOrderEvent(order);

    verifyNoInteractions(paymentService, outboxEventPublisher);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  @DisplayName("publishes the existing outbox event for each completed payment")
  void publishesCompletedPaymentEvents(boolean paymentsAlreadyLoaded) {
    Order order = OrderFixtureFactory.createSingle(OrderStatus.CONFIRMED);
    Payment firstPayment =
        paymentFor(
            order, PaymentStatus.COMPLETED, TOTAL_AMOUNT.subtract(POINT_AMOUNT), BigDecimal.ZERO);
    Payment secondPayment =
        PaymentFixtureFactory.createSingle(
            11L,
            order.getId(),
            order.getMemberId(),
            PaymentStatus.COMPLETED,
            BigDecimal.ZERO,
            POINT_AMOUNT);
    OutboxEvent firstEvent =
        OutboxEvent.fromDatabase(
            101L, EventType.PAY_ORDER, "{\"payment_id\":10}", OutboxEventStatus.INIT);
    OutboxEvent secondEvent =
        OutboxEvent.fromDatabase(
            102L, EventType.PAY_ORDER, "{\"payment_id\":11}", OutboxEventStatus.INIT);
    firstPayment.linkOutboxEvent(firstEvent);
    secondPayment.linkOutboxEvent(secondEvent);
    Payments payments = Payments.of(List.of(firstPayment, secondPayment));

    if (paymentsAlreadyLoaded) {
      order.insertPaymentsIfEmpty(payments, false);
    } else {
      when(paymentService.getPaymentsWithOutboxEvent(order)).thenReturn(payments);
    }

    orderPaymentFacade.publishCompletedPayOrderEvent(order);

    assertThat(order.mapPayments(Payment::getId))
        .containsExactly(firstPayment.getId(), secondPayment.getId());
    ArgumentCaptor<OutboxEvent> events = ArgumentCaptor.forClass(OutboxEvent.class);
    InOrder inOrder = inOrder(paymentService, outboxEventPublisher);
    if (paymentsAlreadyLoaded) {
      verifyNoInteractions(paymentService);
    } else {
      inOrder.verify(paymentService).getPaymentsWithOutboxEvent(order);
    }
    inOrder.verify(outboxEventPublisher, times(2)).publish(events.capture());
    inOrder.verifyNoMoreInteractions();
    assertThat(events.getAllValues())
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactly(firstEvent, secondEvent);
  }

  @Test
  @DisplayName("handles a missing outbox event without publishing or throwing")
  void handlesMissingPaymentOutboxEvent() {
    Order order = OrderFixtureFactory.createSingle(OrderStatus.CONFIRMED);
    Payment firstPayment =
        paymentFor(
            order, PaymentStatus.COMPLETED, TOTAL_AMOUNT.subtract(POINT_AMOUNT), BigDecimal.ZERO);
    firstPayment.linkOutboxEvent(
        OutboxEvent.fromDatabase(101L, EventType.PAY_ORDER, "{}", OutboxEventStatus.INIT));
    Payment secondPayment =
        PaymentFixtureFactory.createSingle(
            11L,
            order.getId(),
            order.getMemberId(),
            PaymentStatus.COMPLETED,
            BigDecimal.ZERO,
            POINT_AMOUNT);
    Payments payments = Payments.of(List.of(firstPayment, secondPayment));
    order.insertPaymentsIfEmpty(payments, false);

    assertThatCode(() -> orderPaymentFacade.publishCompletedPayOrderEvent(order))
        .doesNotThrowAnyException();

    verifyNoInteractions(paymentService, outboxEventPublisher);
  }

  private static Payment paymentFor(
      Order order, PaymentStatus status, BigDecimal payAmount, BigDecimal pointAmount) {
    return PaymentFixtureFactory.createSingle(
        order.getId(), order.getMemberId(), status, payAmount, pointAmount);
  }

  private static PaymentTransaction transactionFor(
      Payment payment, PaymentTransactionStatus status, PaySequenceType sequenceType) {
    return PaymentTransactionFixtureFactory.createSingleRandom(
        payment.getMemberId(),
        payment.getId(),
        payment.getOrderId(),
        payment.getPayMethodId(),
        status,
        sequenceType);
  }
}
