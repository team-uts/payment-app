package dev.teamuts.payment.infra.pg.dto;

import com.stripe.model.v2.core.Account;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtPGAccountResponse {
  private Long memberId;
  private String pgAccountId;
  private PGProviderType pgProvider;

  public static ExtPGAccountResponse stripeAccountV2(Account account, String userId) {
    return new ExtPGAccountResponse(Long.parseLong(userId), account.getId(), PGProviderType.STRIPE);
  }
}
