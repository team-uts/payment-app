package dev.teamuts.payment.persistence.order.mapper;

import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.persistence.order.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {
  public OrderJpaEntity convertToJpaEntity(Order model) {
    return OrderJpaEntity.fromDomain(model);
  }

  public Order convertToDomainModel(OrderJpaEntity entity) {
    return Order.fromDatabase(
        entity.getId(),
        entity.getExtOrderNum(),
        entity.getServiceType(),
        entity.getMemberId(),
        entity.getAmount(),
        entity.getCurrency(),
        entity.getStatus());
  }
}
