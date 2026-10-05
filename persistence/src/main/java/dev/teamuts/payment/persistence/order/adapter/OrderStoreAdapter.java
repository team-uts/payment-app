package dev.teamuts.payment.persistence.order.adapter;

import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.port.persistence.OrderStorePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.order.entity.OrderJpaEntity;
import dev.teamuts.payment.persistence.order.mapper.OrderConverter;
import dev.teamuts.payment.persistence.order.repository.OrderRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class OrderStoreAdapter implements OrderStorePort {
  private final OrderRepository orderRepository;
  private final OrderConverter orderConverter;

  @Override
  public Order store(Order order) {
    OrderJpaEntity entity = orderConverter.convertToJpaEntity(order);

    OrderJpaEntity savedEntity = orderRepository.save(entity);

    return orderConverter.convertToDomainModel(savedEntity);
  }
}
