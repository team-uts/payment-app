package dev.teamuts.payment.infra.pg.clients;

import com.stripe.StripeClient;
import com.stripe.model.SetupIntent;
import com.stripe.model.v2.core.Account;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.param.v2.core.AccountCreateParams;
import com.stripe.param.v2.core.AccountCreateParams.Identity.EntityType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.CreateExtPGAccountRequestDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.infra.pg.constant.ExtPGApiType;
import dev.teamuts.payment.infra.pg.dto.ExtPGAccountResponse;
import dev.teamuts.payment.infra.pg.dto.ExtPGBaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StripePGApiClientService implements PGApiClientService {
  private final StripeClient stripeClient;

  @Override
  public ExtPGBaseResponse<ExtPGAccountResponse> createAccount(
      CreateExtPGAccountRequestDto request) {
    ExtPGApiType extPGApiType = ExtPGApiType.CREATE_ACCOUNT_V2;
    AccountCreateParams params =
        AccountCreateParams.builder()
            .setContactEmail(request.getEmail())
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
            .putMetadata(extPGApiType.getMetadataKey(), request.getMemberId().toString())
            .build();

    try {
      Account account = stripeClient.v2().core().accounts().create(params);
      String userId = account.getMetadata().get(extPGApiType.getMetadataKey());

      return ExtPGBaseResponse.succeeded(
          ExtPGAccountResponse.stripeAccountV2(account, userId), ExtPGApiType.CREATE_ACCOUNT_V2);
    } catch (Exception e) {
      log.error(e.getMessage());

      return ExtPGBaseResponse.failed(
          ExtPGApiType.CREATE_ACCOUNT_V2,
          "Failed to create Stripe Account (memberId: %d)".formatted(request.getMemberId()));
    }
  }

  @Override
  public void initializePaymentMethodSetup(PGAccount pgAccount) {
    SetupIntentCreateParams params =
        SetupIntentCreateParams.builder()
            .setCustomerAccount(pgAccount.getPgAccountId())
            .setAutomaticPaymentMethods(
                SetupIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
            .build();

    try {
      SetupIntent setupIntent = stripeClient.v1().setupIntents().create(params);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean supports(PGProviderType key) {
    return key == PGProviderType.STRIPE;
  }
}
