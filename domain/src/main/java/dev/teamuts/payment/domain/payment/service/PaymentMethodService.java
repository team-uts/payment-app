package dev.teamuts.payment.domain.payment.service;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentMethodReaderPort;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentMethodStorePort;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {
  private final PaymentMethodReaderPort paymentMethodReaderPort;
  private final PaymentMethodStorePort paymentMethodStorePort;

  public List<PaymentMethod> getPaymentMethodList(Long memberId) {
    return paymentMethodReaderPort.retrievePaymentMethodsByMemberId(memberId);
  }

  @AppTransactional
  public PaymentMethod registerNewPaymentMethod(ExtPGPaymentMethodDto extPGPaymentMethod) {
    PaymentMethod paymentMethod = PaymentMethod.newActiveMethod(extPGPaymentMethod);

    return paymentMethodStorePort.store(paymentMethod);
  }

  public boolean isAlreadyPaymentMethodRegistered(
      Long memberId, String pgProviderToken, PGProviderType pgProvider) {
    return paymentMethodReaderPort.existsByParameters(memberId, pgProviderToken, pgProvider);
  }
}
