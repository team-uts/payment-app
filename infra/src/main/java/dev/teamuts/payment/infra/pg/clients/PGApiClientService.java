package dev.teamuts.payment.infra.pg.clients;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.CreateExtPGAccountRequestDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.infra.pg.dto.BaseExtPGResponse;
import dev.teamuts.payment.infra.pg.dto.ExtPGAccountResponse;
import dev.teamuts.payment.infra.pg.dto.ExtPaymentMethodProcessResponse;
import dev.teamuts.payment.shared.provider.ProviderService;

public interface PGApiClientService extends ProviderService<PGProviderType> {
  BaseExtPGResponse<ExtPGAccountResponse> createAccount(CreateExtPGAccountRequestDto request);

  BaseExtPGResponse<ExtPaymentMethodProcessResponse> initializePaymentMethodSetup(
      PGAccount pgAccount);
}
