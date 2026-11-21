package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodDto;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.model.PgAccount;
import dev.teamuts.shared.utils.provider.ProviderService;

public interface ExternalPgServicePort extends ProviderService<PgProviderType> {
  // Account (Customer)
  ExtPGAccountDto createNewAccount(Long memberId, String email);

  // Payment Method
  ExtPGPaymentMethodOperationDto setupPaymentMethodRequest(PgAccount pgAccount);

  ExtPGPaymentMethodDto retrievePGPaymentMethod(PgAccount pgAccount, String pgPaymentMethodId);

  // Webhook
  String getWebhookHeaderName();

  ExtPGWebhookEventDto parseWebhookEvent(PGRequestType requestType, String payload, String secret);
}
