package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.model.Order;
import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentReaderPort;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentStorePort;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentUpdatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentReaderPort paymentReaderPort;
  private final PaymentStorePort paymentStorePort;
  private final PaymentUpdatePort paymentUpdatePort;

  public Payment getProcessingPaymentOrCreate(
      Order order, PaymentMethod paymentMethod, Money pointAmount) {
    Payment payment =
        paymentReaderPort
            .retrievePaymentByOrderIdAndServiceType(
                order.getId(), order.getServiceType(), paymentMethod.getMethodType())
            .orElseGet(() -> createProcessing(order, paymentMethod, pointAmount));

    // check if the payment is still in processing status.
    payment.checkProcessingStatus();

    return payment;
  }

  public void integrateTransactions(Payment payment, PaymentTransactions transactions) {
    payment.integrateTransactions(transactions);

    // update payment status
    paymentUpdatePort.updateStatus(payment);
  }

  private Payment createProcessing(Order order, PaymentMethod paymentMethod, Money pointAmount) {
    Payment newPayment =
        paymentMethod.isPointType()
            ? Payment.processingOnlyPoint(order, pointAmount)
            : Payment.processing(order, paymentMethod, pointAmount);

    return paymentStorePort.store(newPayment);
  }
}
