package dev.teamuts.payment.domain.payment.model.sub;

import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.shared.domain.model.BaseCollection;
import java.util.List;

public class PaymentRequests extends BaseCollection<PaymentRequest> {
  private PaymentRequests(List<PaymentRequest> elements) {
    super(elements);
  }

  public static PaymentRequests createSingleCustomerRequest(PayOrderCommand command) {
    return new PaymentRequests(List.of(PaymentRequest.customerRequest(command)));
  }
}
