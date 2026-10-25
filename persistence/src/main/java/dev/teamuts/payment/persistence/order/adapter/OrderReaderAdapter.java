package dev.teamuts.payment.persistence.order.adapter;

import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.order.port.persistence.OrderReaderPort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.order.mapper.OrderConverter;
import dev.teamuts.payment.persistence.order.repository.OrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class OrderReaderAdapter implements OrderReaderPort {
  private final OrderRepository orderRepository;
  private final OrderConverter orderConverter;

  @Override
  public Optional<Order> retrieveByExtOrderNumNullable(String extOrderNum) {
    return orderRepository.findByExtOrderNum(extOrderNum).map(orderConverter::convertToDomainModel);
  }
}
