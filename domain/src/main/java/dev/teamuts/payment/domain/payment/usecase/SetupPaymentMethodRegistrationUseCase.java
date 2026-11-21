package dev.teamuts.payment.domain.payment.usecase;

import static dev.teamuts.shared.data.annotation.LockKeyPath.*;

import dev.teamuts.payment.domain.common.lock.PaymentLockGroup;
import dev.teamuts.payment.domain.payment.dto.app.SetupPaymentMethodCommand;
import dev.teamuts.payment.domain.pg.dto.app.ExtPGOperationInfo.SetupPaymentMethodInfo;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.model.PgAccount;
import dev.teamuts.payment.domain.pg.service.PgAccountService;
import dev.teamuts.payment.domain.pg.service.PgExternalRequestService;
import dev.teamuts.shared.data.annotation.LockKeyPath;
import dev.teamuts.shared.data.annotation.TryLock;
import dev.teamuts.shared.data.constant.LockKeyType;
import dev.teamuts.shared.domain.annotation.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SetupPaymentMethodRegistrationUseCase {
  private final PgAccountService pgAccountService;
  private final PgExternalRequestService pgExternalRequestService;

  @TryLock(
      group = PaymentLockGroup.SETUP_PAYMENT_METHOD_NAME,
      keyPaths = {
        @LockKeyPath(
            keyType = LockKeyType.OBJECT,
            keyParams = @LockKeyParam(key = "command", field = "memberId")),
      })
  public SetupPaymentMethodInfo execute(SetupPaymentMethodCommand command) {
    // get PG Account from database
    PgAccount pgAccount =
        pgAccountService
            .getPGAccountByMemberIdNullable(command.memberId())
            .orElseGet(
                // if not present, create new PG Account in both pgProvider and database
                () ->
                    pgAccountService.registerPGAccount(
                        command.memberId(), command.email(), command.pgProvider()));

    // initialize payment method setup with PG and store the request in database
    PGExternalRequest pgExternalRequest =
        pgExternalRequestService.storeInitPaymentMethodRequest(pgAccount);

    return SetupPaymentMethodInfo.of(pgExternalRequest);
  }
}
