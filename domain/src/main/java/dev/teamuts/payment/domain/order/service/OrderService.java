package dev.teamuts.payment.domain.order.service;

import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.port.persistence.OrderStorePort;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderStorePort orderStorePort;

  @AppTransactional
  public Order initializeOrder(Long memberId, PayOrderCommand command) {
    Order newOrder = Order.init(memberId, command);

    return orderStorePort.store(newOrder);
  }
}
