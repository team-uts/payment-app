package dev.teamuts.payment.domain.payment.facade.pay;

import dev.teamuts.payment.domain.payment.constant.PaySequenceType;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.shared.provider.ProviderService;

public interface PayTransactionProcessor extends ProviderService<PaySequenceType> {
  PaymentTransaction pay(PaymentTransaction transaction);
}
