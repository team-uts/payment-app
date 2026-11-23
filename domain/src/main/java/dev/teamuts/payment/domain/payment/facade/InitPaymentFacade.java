package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentRequest;
import dev.teamuts.payment.domain.payment.model.PaymentRequests;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.payment.service.PaymentService;
import dev.teamuts.payment.domain.point.service.PointService;
import dev.teamuts.shared.data.annotation.AppTransactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitPaymentFacade {
  private final PaymentService paymentService;
  private final PaymentMethodService paymentMethodService;
  private final PointService pointService;

  /**
   * The reason why trying to retrieve first is to avoid creating duplicate records. This kind of
   * process is for retrying the payment which couldn't be completed due to some issues.<br>
   */
  @AppTransactional
  public Payments getProcessingPaymentsOrCreate(Order order, PaymentRequests paymentRequests) {
    List<Payment> payments =
        paymentRequests.mapToList(request -> getProcessingPaymentOrCreate(order, request));

    return Payments.of(payments);
  }

  private Payment getProcessingPaymentOrCreate(Order order, PaymentRequest paymentRequest) {
    // If it has any point usage, validate the point status of the member. (Request to Point API)
    if (paymentRequest.hasAnyPointUsage()) {
      pointService.checkPointStatus(order.getMemberId(), paymentRequest.getPointAmount());
    }

    // Case 1.
    // If it has only point usage, retrieve or create Payment (PROCESSING) with Point PaymentMethod
    if (paymentRequest.hasOnlyPointUsage()) {
      return paymentService.getProcessingPaymentOrCreate(
          order, PaymentMethod.point(order.getMemberId()), paymentRequest);
    }

    // Case 2.
    // If it is the usual case, retrieve PaymentMethod to create and process new Payment
    PaymentMethod paymentMethod =
        paymentMethodService.getActivePaymentMethodById(paymentRequest.getPayMethodId());

    // Validate PaymentMethod
    paymentMethod.checkIfBelongsToMember(order.getMemberId());

    // Retrieve or Create Payment (PROCESSING)
    return paymentService.getProcessingPaymentOrCreate(order, paymentMethod, paymentRequest);
  }
}
