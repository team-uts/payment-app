package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.model.PgAccount;
import dev.teamuts.payment.domain.pg.model.sub.ExtPgPaymentTransaction;
import dev.teamuts.shared.utils.provider.ProviderService;

public interface ExternalPgPaymentServicePort extends ProviderService<PgProviderType> {
  // Payment
  ExtPgPaymentTransaction retrievePGPaymentTransaction(PaymentTransaction transaction);

  ExtPgPaymentTransaction createPGTransaction(
      PaymentTransaction transaction, PgAccount pgAccount, PaymentMethod paymentMethod);

  ExtPgPaymentTransaction confirmPayment(PaymentTransaction transaction);
}
