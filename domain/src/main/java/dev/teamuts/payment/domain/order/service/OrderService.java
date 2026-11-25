package dev.teamuts.payment.domain.order.service;

import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.error.OrderErrorCode;
import dev.teamuts.payment.domain.order.error.OrderException;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.port.persistence.OrderReaderPort;
import dev.teamuts.payment.domain.order.port.persistence.OrderStorePort;
import dev.teamuts.payment.domain.order.port.persistence.OrderUpdatePort;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.shared.data.annotation.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderReaderPort orderReaderPort;
  private final OrderStorePort orderStorePort;
  private final OrderUpdatePort orderUpdatePort;

  @AppTransactional
  public Order getPaymentOrderOrCreateInit(Long memberId, PayOrderCommand command) {
    Order order =
        orderReaderPort
            .retrieveOrNullByUnique(command.extOrderNum(), command.serviceType())
            .orElseGet(() -> {
              Order newOrder = Order.init(memberId, command);

              return orderStorePort.store(newOrder);
            });

    if (order.isNotTheSameMember(memberId)) {
      throw OrderException.of(OrderErrorCode.MEMBER_MISMATCH, order.getExtOrderNum(), memberId);
    }

    return order;
  }

  @AppTransactional
  public void prepareForPayment(Order order) {
    if (order.isPaymentPending()) {
      return;
    }

    order.paymentPending();

    orderUpdatePort.updateStatus(order);
  }

  @AppTransactional
  public void applyPaymentResults(Order order, Payments payments) {
    // update order status based on the payment status
    order.applyNewPayments(payments);

    orderUpdatePort.updateStatus(order);
  }
}
