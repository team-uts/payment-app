package dev.teamuts.payment.domain.pg.facade;

import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.port.infra.provider.ExternalPGServiceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMethodSetupPGEventProcessor extends BasePGWebhookEventProcessor {
  private final PaymentMethodService paymentMethodService;
  private final ExternalPGServiceProvider pgServiceProvider;

  @Override
  public boolean supports(PGRequestType key) {
    return key == PGRequestType.PAYMENT_METHOD_SETUP;
  }

  /**
   * Processes a payment method setup event.
   *
   * @param event pgProviderToken
   */
  @Override
  public void process(ExtPGWebhookEventDto event) {
    // TODO: Implement the logic to process the payment method setup event.
    // Store new PaymentMethod based on the event data ()

    // Retrieve the PG PaymentMethod Information from PG provider using the pgProviderToken
    // to get the necessary details to store a relevant Model (e.g., Card)
  }
}
