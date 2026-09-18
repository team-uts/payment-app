package dev.teamuts.payment.domain.paymentmethod.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.paymentmethod.command.SetupPaymentMethodCommand;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.service.PGAccountService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SetupPaymentMethodRegistrationUseCase {
  private final PGAccountService pgAccountService;

  public String execute(SetupPaymentMethodCommand command) {
    PGAccount pgAccount =
        pgAccountService
            .findPGAccountByMemberId(command.memberId())
            .orElseGet(
                () ->
                    pgAccountService.createPGAccount(
                        command.memberId(), command.email(), PGProviderType.STRIPE));

    return "";
  }
}
