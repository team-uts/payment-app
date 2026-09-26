package dev.teamuts.payment.domain.payment.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.payment.dto.SetupPaymentMethodCommand;
import dev.teamuts.payment.domain.pg.dto.PGExtOperationInfo.SetupPaymentMethodInfo;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.service.PGAccountService;
import dev.teamuts.payment.domain.pg.service.PGExternalRequestService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SetupPaymentMethodRegistrationUseCase {
  private final PGAccountService pgAccountService;
  private final PGExternalRequestService pgExternalRequestService;

  // TODO: add Locking mechanism to prevent concurrent setup requests for the same memberId
  public SetupPaymentMethodInfo execute(SetupPaymentMethodCommand command) {
    // get PG Account from database
    PGAccount pgAccount =
        pgAccountService
            .findPGAccountByMemberId(command.memberId())
            .orElseGet(
                // if not present, create new PG Account in both pgProvider and database
                () ->
                    pgAccountService.createPGAccount(
                        command.memberId(), command.email(), command.pgProvider()));

    // initialize payment method setup with PG and store the request in database
    PGExternalRequest pgExternalRequest =
        pgExternalRequestService.initializePaymentMethodRequest(pgAccount);

    return SetupPaymentMethodInfo.of(pgExternalRequest);
  }
}
