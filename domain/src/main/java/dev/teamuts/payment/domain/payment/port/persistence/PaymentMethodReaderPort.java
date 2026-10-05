package dev.teamuts.payment.domain.payment.port.persistence;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import java.util.List;

public interface PaymentMethodReaderPort {
  List<PaymentMethod> retrievePaymentMethodsByMemberId(Long memberId);

  PaymentMethod retrievePaymentMethodById(Long id);

  boolean existsByParameters(Long memberId, String pgProviderToken, PGProviderType pgProvider);
}
