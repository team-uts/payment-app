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
import dev.teamuts.payment.infra.pg.constant.ExtPGOperationType;
import dev.teamuts.payment.infra.pg.dto.BaseExtPGResponse;
import dev.teamuts.payment.infra.pg.dto.ExtPGAccountResponse;
import dev.teamuts.payment.infra.pg.dto.ExtPaymentMethodProcessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StripePGApiClientService implements PGApiClientService {
  private final StripeClient stripeClient;

  @Override
  public BaseExtPGResponse<ExtPGAccountResponse> createAccount(
      CreateExtPGAccountRequestDto request) {
    ExtPGOperationType operationType = ExtPGOperationType.CREATE_ACCOUNT_V2;

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
            .putMetadata(operationType.getMetadataKey(), request.getMemberId().toString())
            .build();

    try {
      Account account = stripeClient.v2().core().accounts().create(params);
      String userId = account.getMetadata().get(operationType.getMetadataKey());

      return BaseExtPGResponse.succeeded(
          ExtPGAccountResponse.stripeAccountV2(account, userId),
          ExtPGOperationType.CREATE_ACCOUNT_V2);
    } catch (Exception e) {
      log.error(e.getMessage());

      return BaseExtPGResponse.failed(
          ExtPGOperationType.CREATE_ACCOUNT_V2,
          "Failed to create Stripe Account (memberId: %d)".formatted(request.getMemberId()));
    }
  }

  @Override
  public BaseExtPGResponse<ExtPaymentMethodProcessResponse> initializePaymentMethodSetup(
      PGAccount pgAccount) {
    ExtPGOperationType operationType = ExtPGOperationType.CREATE_SETUP_INTENT;

    // Parms: Stripe Account ID
    // Parms: Member ID (for metadata)
    SetupIntentCreateParams params =
        SetupIntentCreateParams.builder()
            .setCustomerAccount(pgAccount.getPgAccountId())
            .setAutomaticPaymentMethods(
                SetupIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
            .putMetadata(operationType.getMetadataKey(), pgAccount.getMemberId().toString())
            .build();

    try {
      SetupIntent setupIntent = stripeClient.v1().setupIntents().create(params);
      String userId = setupIntent.getMetadata().get(operationType.getMetadataKey());

      return BaseExtPGResponse.succeeded(
          ExtPaymentMethodProcessResponse.stripeSetupIntent(setupIntent, userId), operationType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean supports(PGProviderType key) {
    return key == PGProviderType.STRIPE;
  }
}
