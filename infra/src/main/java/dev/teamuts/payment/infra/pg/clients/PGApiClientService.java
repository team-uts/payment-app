package dev.teamuts.payment.infra.pg.clients;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.CreateExtPGAccountRequestDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.infra.pg.dto.ExtPGAccountResponse;
import dev.teamuts.payment.infra.pg.dto.ExtPGBaseResponse;
import dev.teamuts.payment.shared.provider.ProviderService;

public interface PGApiClientService extends ProviderService<PGProviderType> {
  ExtPGBaseResponse<ExtPGAccountResponse> createAccount(CreateExtPGAccountRequestDto request);

  void initializePaymentMethodSetup(PGAccount pgAccount);
}
