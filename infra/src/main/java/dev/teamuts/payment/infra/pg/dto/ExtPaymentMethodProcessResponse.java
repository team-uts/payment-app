package dev.teamuts.payment.infra.pg.dto;

import com.stripe.model.SetupIntent;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtPaymentMethodProcessResponse {
  private Long memberId;
  private String pgAccountId;
  private PGProviderType pgProvider;
  private String pgOperationId;
  private String pgProviderSecret;

  public static ExtPaymentMethodProcessResponse stripeSetupIntent(
      SetupIntent setupIntent, String userId) {
    return new ExtPaymentMethodProcessResponse(
        Long.parseLong(userId),
        setupIntent.getCustomer(),
        PGProviderType.STRIPE,
        setupIntent.getId(),
        setupIntent.getClientSecret());
  }
}
