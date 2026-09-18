package dev.teamuts.payment.infra.pg.adapter;

import dev.teamuts.payment.domain.pg.dto.CreateExtPGAccountRequestDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.infra.ExtPaymentGatewayApiPort;
import dev.teamuts.payment.infra.common.annotation.PGAdapter;
import dev.teamuts.payment.infra.pg.clients.PGApiClientServiceProvider;
import dev.teamuts.payment.infra.pg.dto.ExtPGAccountResponse;
import lombok.RequiredArgsConstructor;

@PGAdapter
@RequiredArgsConstructor
public class ExtPaymentGatewayApiAdapter implements ExtPaymentGatewayApiPort {
  private final PGApiClientServiceProvider pgClientProvider;

  @Override
  public ExtPGAccountDto createNewAccount(CreateExtPGAccountRequestDto request) {
    ExtPGAccountResponse extPGAccountResponse =
        pgClientProvider.getInstance(request.getPgProvider()).createAccount(request).orElseThrow();

    return ExtPGAccountDto.builder()
        .memberId(extPGAccountResponse.getMemberId())
        .pgAccountId(extPGAccountResponse.getPgAccountId())
        .pgProvider(extPGAccountResponse.getPgProvider())
        .build();
  }

  @Override
  public void setupPaymentMethodRequest(PGAccount pgAccount) {
    pgClientProvider.getInstance(pgAccount.getPgProvider());
  }

  @Override
  public void setupPaymentRequest() {}

  @Override
  public void confirmPaymentRequest() {}
}
