package dev.teamuts.payment.infra.pg.dto;

import com.stripe.model.Customer;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtPGAccountResponse {
  private Long memberId;
  private String pgAccountId;
  private PGProviderType pgProvider;

  public static ExtPGAccountResponse stripeAccountV2(Customer customer, String userId) {
    return new ExtPGAccountResponse(
        Long.parseLong(userId), customer.getId(), PGProviderType.STRIPE);
  }
}
