package dev.teamuts.payment.domain.order.service;

import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.port.persistence.OrderReaderPort;
import dev.teamuts.payment.domain.order.port.persistence.OrderStorePort;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderReaderPort orderReaderPort;
  private final OrderStorePort orderStorePort;

  @AppTransactional
  public Order getInitPaymentOrderOrCreate(Long memberId, PayOrderCommand command) {
    Order order =
        orderReaderPort
            .retrieveByExtOrderNumNullable(command.extOrderNum())
            .orElseGet(
                () -> {
                  Order newOrder = Order.initPayment(memberId, command);

                  return orderStorePort.store(newOrder);
                });

    if (order.isNotTheSameMember(memberId) && order.isPaymentPending()) {
      throw new RuntimeException(
          "Order (extOrderNum: %s) is not valid for processing payment for memberId %s"
              .formatted(command.extOrderNum(), memberId));
    }

    return order;
  }
}
