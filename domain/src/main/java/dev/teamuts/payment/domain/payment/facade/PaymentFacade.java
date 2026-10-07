package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.payment.domain.point.service.PointService;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
  private final PaymentService paymentService;
  private final PaymentMethodService paymentMethodService;
  private final PointService pointService;

  @AppTransactional
  public Payment getProcessingPaymentOrCreate(Order order, PayOrderCommand command) {
    // If it has any point usage, validate the point status of the member. (Request to Point API)
    if (command.hasAnyPointUsage()) {
      pointService.checkPointStatus(order.getMemberId(), command.pointAmount());
    }

    // If it has only point usage, retrieve or create Payment (PROCESSING) with Point PaymentMethod
    if (command.hasOnlyPointUsage()) {
      return paymentService.getProcessingPaymentOrCreate(
          order, PaymentMethod.point(order.getMemberId()), command.pointAmount());
    }

    // If it is the usual case,
    // retrieve PaymentMethod to create and process new Payment
    PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(command.payMethodId());

    // Validate PaymentMethod
    paymentMethod.checkIfBelongsToMember(order.getMemberId());

    // Retrieve or Create Payment (PROCESSING)
    return paymentService.getProcessingPaymentOrCreate(order, paymentMethod, command.pointAmount());
  }
}
