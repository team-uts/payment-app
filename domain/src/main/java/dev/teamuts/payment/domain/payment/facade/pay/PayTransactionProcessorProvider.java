package dev.teamuts.payment.domain.payment.facade.pay;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PayTransactionProcessorProvider
    extends Provider<PaySequenceType, PayTransactionProcessor> {

  protected PayTransactionProcessorProvider(List<PayTransactionProcessor> services) {
    super(services);
  }
}
