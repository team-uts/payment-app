package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodDto;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.shared.provider.ProviderService;

public interface ExternalPGServicePort extends ProviderService<PGProviderType> {
  // Account (Customer)
  ExtPGAccountDto createNewAccount(Long memberId, String email);

  // Payment Method
  ExtPGPaymentMethodOperationDto setupPaymentMethodRequest(PGAccount pgAccount);

  ExtPGPaymentMethodDto retrievePGPaymentMethod(PGAccount pgAccount, String pgPaymentMethodId);

  // Webhook
  String getWebhookHeaderName();

  ExtPGWebhookEventDto parseWebhookEvent(PGRequestType requestType, String payload, String secret);
}
