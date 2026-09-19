package dev.teamuts.payment.domain.paymentmethod.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.paymentmethod.dto.SetupPaymentMethodCommand;
import dev.teamuts.payment.domain.pg.dto.CreateExtPGAccountRequestDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.service.PGAccountService;
import dev.teamuts.payment.domain.pg.service.PGExternalRequestService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SetupStripePaymentMethodRegistrationUseCase {
  private final PGAccountService pgAccountService;
  private final PGExternalRequestService pgExternalRequestService;

  public String execute(SetupPaymentMethodCommand command) {
    // get PG Account from database
    PGAccount pgAccount =
        pgAccountService
            .findPGAccountByMemberId(command.memberId())
            .orElseGet(
                // if not present, create new PG Account in both pgProvider and database
                () ->
                    pgAccountService.createPGAccount(CreateExtPGAccountRequestDto.stripe(command)));

    // initialize payment method setup with PG and store the request in database
//    PGExternalRequest pgExternalRequest =
//        pgExternalRequestService.initializePaymentMethodRequest(pgAccount);

    return "";
  }
}
