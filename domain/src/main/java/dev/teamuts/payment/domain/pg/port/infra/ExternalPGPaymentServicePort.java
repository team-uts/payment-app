package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.ExtPgPaymentTransaction;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.shared.provider.ProviderService;

public interface ExternalPGPaymentServicePort extends ProviderService<PGProviderType> {
  // Payment
  ExtPgPaymentTransaction retrievePGPaymentTransaction(PaymentTransaction transaction);

  ExtPgPaymentTransaction createPGTransaction(
      PaymentTransaction transaction, PGAccount pgAccount, PaymentMethod paymentMethod);

  ExtPgPaymentTransaction confirmPayment(PaymentTransaction transaction);
}
