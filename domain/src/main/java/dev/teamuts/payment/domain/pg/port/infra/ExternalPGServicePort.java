package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.shared.provider.ProviderService;

public interface ExternalPGServicePort extends ProviderService<PGProviderType> {
  ExtPGAccountDto createNewAccount(Long memberId, String email);

  ExtPGPaymentMethodOperationDto setupPaymentMethodRequest(PGAccount pgAccount);

  void setupPaymentRequest();

  void confirmPaymentRequest();

  String getWebhookSecret(PGRequestType requestType, PGProviderType pgProvider);
}
