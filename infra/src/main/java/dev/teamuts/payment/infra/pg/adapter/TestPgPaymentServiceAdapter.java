package dev.teamuts.payment.infra.pg.adapter;

import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.model.PgAccount;
import dev.teamuts.payment.domain.pg.model.sub.ExtPgPaymentTransaction;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPgPaymentServicePort;
import dev.teamuts.shared.config.AppProfile;
import dev.teamuts.shared.domain.annotation.InfraAdapter;
import java.util.UUID;
import org.springframework.context.annotation.Profile;

/** only for local test. */
@InfraAdapter
@Profile(AppProfile.LOCAL)
public class TestPgPaymentServiceAdapter implements ExternalPgPaymentServicePort {
  private static final String TEST_PG_TRANSACTION_ID_PREFIX = "TEST_PG_";

  @Override
  public ExtPgPaymentTransaction retrievePGPaymentTransaction(PaymentTransaction transaction) {
    return ExtPgPaymentTransaction.fromPgTest(
        transaction,
        transaction.getPgTransactionId(),
        transaction.getPgResponseCode(),
        transaction.getPgErrorCode(),
        transaction.getPgErrorMessage(),
        transaction.getDetailMessage(),
        PaymentTransactionStatus.READY);
  }

  @Override
  public ExtPgPaymentTransaction createPGTransaction(
      PaymentTransaction transaction, PgAccount pgAccount, PaymentMethod paymentMethod) {
    return ExtPgPaymentTransaction.fromPgTest(
        transaction,
        TEST_PG_TRANSACTION_ID_PREFIX + UUID.randomUUID(),
        "200",
        null,
        null,
        "CREATE_PG_TRANSACTION",
        PaymentTransactionStatus.READY);
  }

  @Override
  public ExtPgPaymentTransaction confirmPayment(PaymentTransaction transaction) {
    return ExtPgPaymentTransaction.fromPgTest(
        transaction,
        TEST_PG_TRANSACTION_ID_PREFIX + UUID.randomUUID(),
        "200",
        null,
        null,
        "CONFIRM_PG_TRANSACTION",
        PaymentTransactionStatus.COMPLETED);
  }

  @Override
  public boolean supports(PgProviderType key) {
    return PgProviderType.TEST == key;
  }
}
