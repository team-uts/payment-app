package dev.teamuts.payment.domain.order.port.persistence;

import dev.teamuts.payment.domain.order.model.Order;
import java.util.Optional;

public interface OrderReaderPort {
  Optional<Order> retrieveByExtOrderNumNullable(String extOrderNum);
}
