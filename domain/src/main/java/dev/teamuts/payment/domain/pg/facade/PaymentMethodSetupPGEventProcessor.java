package dev.teamuts.payment.domain.pg.facade;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.infra.provider.ExternalPGServiceProvider;
import dev.teamuts.payment.domain.pg.service.PGAccountService;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

@Component
@RequiredArgsConstructor
public class PaymentMethodSetupPGEventProcessor extends BasePGWebhookEventProcessor {
  private final ExternalPGServiceProvider pgServiceProvider;
  private final PaymentMethodService paymentMethodService;
  private final PGAccountService pgAccountService;

  @Override
  public boolean supports(PGRequestType key) {
    return key == PGRequestType.PAYMENT_METHOD_SETUP;
  }

  /**
   * Processes a payment method setup event.
   *
   * <p>{@link Propagation#REQUIRES_NEW} This method is executed in a new transaction to process
   * this operation independently. (e.g., to do this operation separately from the *PG webhook
   * event* processing transaction)
   *
   * @param event Webhook Event from Payment Gateway. {@link
   *     ExtPGWebhookEventDto#getPgProviderToken()} = pgPaymentMethodId from PG provider, which is
   *     {@link PaymentMethod#getProviderToken()}.
   */
  @AppTransactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void process(ExtPGWebhookEventDto event) {
    String pgPaymentMethodId = event.getPgProviderToken();

    // Retrieve the PGAccount Information from DB using the memberId
    PGAccount pgAccount = pgAccountService.getPGAccountByMemberIdNotNull(event.getMemberId());

    // Retrieve the PG PaymentMethod Information from PG provider using the pgProviderToken
    // to get the necessary details to store a relevant Model (e.g., Card)
    ExtPGPaymentMethodDto extPGPaymentMethod =
        pgServiceProvider
            .getInstance(event.getPgProvider())
            .retrievePGPaymentMethod(pgAccount, pgPaymentMethodId);

    // Store new PaymentMethod
    paymentMethodService.storeNewPaymentMethod(extPGPaymentMethod);
  }
}
