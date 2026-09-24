package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import java.util.List;

public interface PaymentMethodReaderPort {
  List<PaymentMethod> retrievePaymentMethodsByMemberId(Long memberId);
}
