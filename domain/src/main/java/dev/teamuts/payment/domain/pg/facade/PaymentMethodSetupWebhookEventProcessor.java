package dev.teamuts.payment.domain.pg.facade;

import dev.teamuts.payment.domain.payment.facade.PaymentMethodFacade;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.infra.provider.ExternalPGServiceProvider;
import dev.teamuts.payment.domain.pg.service.PGAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMethodSetupWebhookEventProcessor extends BasePGWebhookEventProcessor {
  private final ExternalPGServiceProvider pgServiceProvider;
  private final PaymentMethodFacade paymentMethodFacade;
  private final PaymentMethodService paymentMethodService;
  private final PGAccountService pgAccountService;

  @Override
  public boolean supports(PGRequestType key) {
    return key == PGRequestType.PAYMENT_METHOD_SETUP;
  }

  @Override
  protected boolean checkAlreadyProcessed(ExtPGWebhookEventDto event) {
    // If the payment method is already registered, we can skip processing this event
    return paymentMethodService.isAlreadyPaymentMethodRegistered(
        event.getMemberId(), event.getPgProviderToken(), event.getPgProvider());
  }

  /**
   * Processes a payment method setup event.
   *
   * @param event Webhook Event from Payment Gateway. {@link
   *     ExtPGWebhookEventDto#getPgProviderToken()} = pgPaymentMethodId from PG provider, which is
   *     {@link PaymentMethod#getProviderToken()}.
   */
  @Override
  public void process(ExtPGWebhookEventDto event) {
    String pgPaymentMethodId = event.getPgProviderToken();

    // Retrieve the PGAccount Information from DB using the memberId
    PGAccount pgAccount = pgAccountService.getPGAccountByMemberId(event.getMemberId());

    // Retrieve the PG PaymentMethod Information from PG provider using the pgProviderToken
    // to get the necessary details to store a relevant Model (e.g., Card)
    ExtPGPaymentMethodDto extPGPaymentMethod =
        pgServiceProvider
            .getInstance(event.getPgProvider())
            .retrievePGPaymentMethod(pgAccount, pgPaymentMethodId);

    // Store new PaymentMethod
    PaymentMethod paymentMethod =
        paymentMethodFacade.registerPaymentMethodWithDetail(extPGPaymentMethod);

    log.info("PaymentMethod successfully stored: {}", paymentMethod.getId());
  }
}
