package dev.teamuts.payment.domain.order.port.persistence;

import dev.teamuts.payment.domain.order.model.Order;

public interface OrderStorePort {
  Order store(Order order);
}
