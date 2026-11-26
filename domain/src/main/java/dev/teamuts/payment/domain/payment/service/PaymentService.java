package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentRequest;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.model.Payments;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentReaderPort;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentStorePort;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentUpdatePort;
import dev.teamuts.shared.data.annotation.AppTransactional;
import dev.teamuts.shared.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentReaderPort paymentReaderPort;
  private final PaymentStorePort paymentStorePort;
  private final PaymentUpdatePort paymentUpdatePort;

  public Payment getProcessingPaymentOrCreate(
      Order order, PaymentMethod paymentMethod, PaymentRequest paymentRequest) {
    Payment payment =
        paymentReaderPort
            .retrievePaymentByOrderIdAndServiceType(
                order.getId(), order.getServiceType(), paymentMethod.getMethodType())
            .orElseGet(() ->
                createProcessing(
                    order,
                    paymentMethod,
                    paymentRequest.getPointAmount(),
                    paymentRequest.getPaymentType()));

    // check if the payment is still in processing status.
    payment.checkProcessingStatus();

    return payment;
  }

  public Payments getPaymentsWithOutboxEvent(Order order) {
    Payments payments = paymentReaderPort.retrieveAllByOrderIdWithOutboxEvent(order.getId());

    // validate all the payment transactions
    payments.forEach(Payment::checkValidOutboxEvent);

    return payments;
  }

  @AppTransactional
  public void integrateTransactions(Payment payment, PaymentTransactions transactions) {
    payment.integrateTransactions(transactions);

    // update payment status
    paymentUpdatePort.updateStatus(payment);
  }

  private Payment createProcessing(
      Order order, PaymentMethod paymentMethod, Money pointAmount, PaymentType paymentType) {
    Payment newPayment =
        paymentMethod.isPointType()
            ? Payment.processingOnlyPoint(order, pointAmount, paymentType)
            : Payment.processing(order, paymentMethod, pointAmount, paymentType);

    return paymentStorePort.store(newPayment);
  }

  @AppTransactional
  public void linkOutboxEvent(Payment payment, OutboxEvent event) {
    payment.linkOutboxEvent(event);

    paymentUpdatePort.updateOutboxEvent(payment);
  }
}
