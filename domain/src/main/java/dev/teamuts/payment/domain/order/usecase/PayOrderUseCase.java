package dev.teamuts.payment.domain.order.usecase;

import static dev.teamuts.shared.data.annotation.LockKeyPath.*;

import dev.teamuts.payment.domain.common.lock.PaymentLockGroup;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.dto.PayOrderInfo;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.service.OrderService;
import dev.teamuts.payment.domain.payment.facade.OrderPaymentFacade;
import dev.teamuts.payment.domain.payment.model.PaymentRequests;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.shared.data.annotation.AppTransactional;
import dev.teamuts.shared.data.annotation.LockKeyPath;
import dev.teamuts.shared.data.annotation.TryLock;
import dev.teamuts.shared.data.constant.LockKeyType;
import dev.teamuts.shared.data.constant.LockTtl;
import dev.teamuts.shared.domain.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;

@UseCase
@RequiredArgsConstructor
public class PayOrderUseCase {
  private final OrderService orderService;
  private final OrderPaymentFacade orderPaymentFacade;

  @TryLock(
      group = PaymentLockGroup.PAY_ORDER_NAME,
      keyPaths = {
        @LockKeyPath(keyType = LockKeyType.NON_OBJECT, keyParams = @LockKeyParam(key = "memberId")),
        @LockKeyPath(
            keyType = LockKeyType.OBJECT,
            keyParams = @LockKeyParam(key = "command", field = "extOrderNum")),
      },
      timeToLive = LockTtl.ONE_MINUTE)
  @AppTransactional(propagation = Propagation.NOT_SUPPORTED)
  public PayOrderInfo execute(Long memberId, PayOrderCommand command) {
    // Retrieve or Create new Order data (PAYMENT_PENDING)
    Order order = orderService.getPaymentOrderOrCreateInit(memberId, command);

    // Process Payment with Order
    Payments resultPayments =
        orderPaymentFacade.processPayOrder(
            order, PaymentRequests.createSingleCustomerRequest(command));

    // Update Order status to "CONFIRMED"
    orderService.updateStatusWithPayment(order, resultPayments);

    // publish the completed pay order event.
    orderPaymentFacade.publishCompletedPayOrderEvent(order);

    return PayOrderInfo.of(order);
  }
}
