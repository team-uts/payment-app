package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.paymentmethod.dto.SetupPaymentMethodCommand;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Request DTO to create New External PG Account */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateExtPGAccountRequestDto {
  Long memberId;
  String email;
  PGProviderType pgProvider;

  public static CreateExtPGAccountRequestDto stripe(SetupPaymentMethodCommand command) {
    return new CreateExtPGAccountRequestDto(
        command.memberId(), command.email(), PGProviderType.STRIPE);
  }
}
