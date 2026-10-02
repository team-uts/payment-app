package dev.teamuts.payment.domain.payment.facade.pm;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodDetailProcessorProvider
    extends Provider<PaymentMethodType, PaymentMethodDetailProcessor> {

  protected PaymentMethodDetailProcessorProvider(List<PaymentMethodDetailProcessor> services) {
    super(services);
  }
}
