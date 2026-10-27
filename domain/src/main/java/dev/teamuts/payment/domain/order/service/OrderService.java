package dev.teamuts.payment.domain.order.service;

import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.port.persistence.OrderReaderPort;
import dev.teamuts.payment.domain.order.port.persistence.OrderStorePort;
import dev.teamuts.payment.domain.order.port.persistence.OrderUpdatePort;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.shared.data.AppTransactional;
import dev.teamuts.payment.shared.error.constant.OrderErrorCode;
import dev.teamuts.payment.shared.error.exception.ErrorArguments;
import dev.teamuts.payment.shared.error.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderReaderPort orderReaderPort;
  private final OrderStorePort orderStorePort;
  private final OrderUpdatePort orderUpdatePort;

  @AppTransactional(propagation = Propagation.REQUIRES_NEW)
  public Order getPaymentOrderOrCreateInit(Long memberId, PayOrderCommand command) {
    Order order =
        orderReaderPort
            .retrieveByExtOrderNumNullable(command.extOrderNum())
            .orElseGet(
                () -> {
                  Order newOrder = Order.initPayment(memberId, command);

                  return orderStorePort.store(newOrder);
                });

    if (order.isNotTheSameMember(memberId)) {
      throw OrderException.of(
          OrderErrorCode.ORDER_MEMBER_MISMATCH,
          ErrorArguments.of(order.getExtOrderNum(), memberId));
    }

    return order;
  }

  @AppTransactional(propagation = Propagation.REQUIRES_NEW)
  public void prepareForPayment(Order order) {
    if (order.isPaymentPending()) {
      return;
    }

    order.paymentPending();

    orderUpdatePort.updateStatus(order);
  }

  public void updateStatusWithPayment(Order order, Payment payment) {
    order.updateStatus(payment.resolveOrderStatus());

    orderUpdatePort.updateStatus(order);
  }
}
