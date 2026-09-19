package dev.teamuts.payment.infra.pg.clients;

import com.stripe.StripeClient;
import com.stripe.model.Customer;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SetupIntentCreateParams;
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
    ExtPGOperationType operationType = ExtPGOperationType.CREATE_CUSTOMER;

    // AccountV2 is not supported by Stripe for sandbox.
    CustomerCreateParams params =
        CustomerCreateParams.builder()
            .setEmail(request.getEmail())
            .setName(request.getMemberId().toString())
            .putMetadata(operationType.getMetadataKey(), request.getMemberId().toString())
            .build();

    try {
      Customer customer = stripeClient.v1().customers().create(params);
      String userId = customer.getName();

      return BaseExtPGResponse.succeeded(
          ExtPGAccountResponse.stripeAccountV2(customer, userId), operationType);
    } catch (Exception e) {
      log.error(e.getMessage());

      return BaseExtPGResponse.failed(
          operationType,
          "Failed to [%s] (memberId: %d)".formatted(operationType.name(), request.getMemberId()));
    }
  }

  @Override
  public BaseExtPGResponse<ExtPaymentMethodProcessResponse> initializePaymentMethodSetup(
      PGAccount pgAccount) {
    ExtPGOperationType operationType = ExtPGOperationType.CREATE_SETUP_INTENT;

    // Parms: **Stripe Customer ID** (not AccountV2 ID)
    // Parms: Member ID (for metadata)
    SetupIntentCreateParams params =
        SetupIntentCreateParams.builder()
            .setCustomer(pgAccount.getPgAccountId())
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
      return BaseExtPGResponse.failed(
          operationType,
          "Failed to [%s] (memberId: %d)".formatted(operationType.name(), pgAccount.getMemberId()));
    }
  }

  @Override
  public boolean supports(PGProviderType key) {
    return key == PGProviderType.STRIPE;
  }
}
