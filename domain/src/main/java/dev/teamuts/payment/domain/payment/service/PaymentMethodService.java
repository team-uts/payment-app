package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentMethodReaderPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {
  private final PaymentMethodReaderPort paymentMethodReaderPort;

  public List<PaymentMethod> getPaymentMethodList(Long memberId) {
    return paymentMethodReaderPort.retrievePaymentMethodsByMemberId(memberId);
  }
}
