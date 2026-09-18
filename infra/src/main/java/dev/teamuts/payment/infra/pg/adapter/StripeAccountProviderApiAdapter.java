package dev.teamuts.payment.infra.pg.adapter;

import com.stripe.StripeClient;
import com.stripe.model.v2.core.Account;
import com.stripe.param.v2.core.AccountCreateParams;
import com.stripe.param.v2.core.AccountCreateParams.Identity.EntityType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.port.infra.PGAccountProviderApiPort;
import dev.teamuts.payment.infra.common.annotation.PGAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PGAdapter
@RequiredArgsConstructor
public class StripeAccountProviderApiAdapter implements PGAccountProviderApiPort {
  private final StripeClient stripeClient;

  @Override
  public String createNewAccount(Long memberId, String email, PGProviderType providerType) {
    AccountCreateParams params =
        AccountCreateParams.builder()
            .setContactEmail(email)
            .setIdentity(
                AccountCreateParams.Identity.builder()
                    .setCountry("au")
                    .setEntityType(EntityType.INDIVIDUAL)
                    .build())
            .setConfiguration(
                AccountCreateParams.Configuration.builder()
                    .setCustomer(
                        AccountCreateParams.Configuration.Customer.builder()
                            .setCapabilities(
                                AccountCreateParams.Configuration.Customer.Capabilities.builder()
                                    .setAutomaticIndirectTax(
                                        AccountCreateParams.Configuration.Customer.Capabilities
                                            .AutomaticIndirectTax.builder()
                                            .setRequested(true)
                                            .build())
                                    .build())
                            .build())
                    .build())
            .addInclude(AccountCreateParams.Include.CONFIGURATION__CUSTOMER)
            .putMetadata("user_id", memberId.toString())
            .build();

    try {
      Account account = stripeClient.v2().core().accounts().create(params);

      return account.getId();
    } catch (Exception e) {
      log.error(e.getMessage());

      throw new RuntimeException(
          "Failed to create Stripe Account (memberId: %d)".formatted(memberId));
    }
  }
}
