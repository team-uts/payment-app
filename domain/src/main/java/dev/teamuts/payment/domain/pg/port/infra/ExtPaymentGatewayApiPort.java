package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.pg.dto.CreateExtPGAccountRequestDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGAccountDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;

public interface ExtPaymentGatewayApiPort {
  ExtPGAccountDto createNewAccount(CreateExtPGAccountRequestDto request);

  void setupPaymentMethodRequest(PGAccount pgAccount);

  void setupPaymentRequest();

  void confirmPaymentRequest();
}
