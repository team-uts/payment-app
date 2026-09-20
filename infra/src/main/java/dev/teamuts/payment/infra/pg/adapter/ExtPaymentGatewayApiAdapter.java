package dev.teamuts.payment.infra.pg.adapter;

import dev.teamuts.payment.domain.pg.dto.CreateExtPGAccountRequestDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.port.infra.ExtPaymentGatewayApiPort;
import dev.teamuts.payment.infra.common.annotation.PGAdapter;
import dev.teamuts.payment.infra.pg.clients.PGApiClientServiceProvider;
import dev.teamuts.payment.infra.pg.dto.BaseExtPGResponse;
import dev.teamuts.payment.infra.pg.dto.ExtPGAccountResponse;
import dev.teamuts.payment.infra.pg.dto.ExtPaymentMethodProcessResponse;
import lombok.RequiredArgsConstructor;

@PGAdapter
@RequiredArgsConstructor
public class ExtPaymentGatewayApiAdapter implements ExtPaymentGatewayApiPort {
  private final PGApiClientServiceProvider pgClientProvider;

  @Override
  public ExtPGAccountDto createNewAccount(CreateExtPGAccountRequestDto request) {
    ExtPGAccountResponse response =
        pgClientProvider.getInstance(request.getPgProvider()).createAccount(request).orElseThrow();

    return ExtPGAccountDto.builder()
        .memberId(response.getMemberId())
        .pgAccountId(response.getPgAccountId())
        .pgProvider(response.getPgProvider())
        .build();
  }

  @Override
  public ExtPGPaymentMethodOperationDto setupPaymentMethodRequest(PGAccount pgAccount) {
    BaseExtPGResponse<ExtPaymentMethodProcessResponse> extBaseResponse =
        pgClientProvider
            .getInstance(pgAccount.getPgProvider())
            .initializePaymentMethodSetup(pgAccount);

    ExtPaymentMethodProcessResponse response = extBaseResponse.orElseThrow();

    return ExtPGPaymentMethodOperationDto.builder()
        .memberId(response.getMemberId())
        .pgOperationId(response.getPgOperationId())
        .pgOperationName(extBaseResponse.getOperationType().name())
        .pgAccountId(response.getPgAccountId())
        .pgProvider(response.getPgProvider())
        .pgProviderSecret(response.getPgProviderSecret())
        .build();
  }

  @Override
  public void setupPaymentRequest() {}

  @Override
  public void confirmPaymentRequest() {}
}
