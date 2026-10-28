package dev.teamuts.payment.persistence.order.adapter;

import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.port.persistence.OrderUpdatePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.common.exception.PersistenceException;
import dev.teamuts.payment.persistence.order.entity.OrderJpaEntity;
import dev.teamuts.payment.persistence.order.repository.OrderRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class OrderUpdateAdapter implements OrderUpdatePort {
  private final OrderRepository orderRepository;

  @Override
  public void updateStatus(Order order) {
    OrderJpaEntity entity =
        orderRepository
            .findLockById(order.getId())
            .orElseThrow(() -> PersistenceException.notFound(order));

    entity.updateStatus(order.getStatus());
  }
}
