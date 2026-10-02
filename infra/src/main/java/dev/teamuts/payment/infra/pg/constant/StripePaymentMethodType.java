package dev.teamuts.payment.infra.pg.constant;

import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethod.Card;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto.ExtPGCardDetail;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto.ExtPGPaymentMethodDetail;
import java.util.EnumSet;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StripePaymentMethodType {
  CARD(
      "card",
      PaymentMethodType.CARD,
      pm -> {
        Card card = pm.getCard();
        return ExtPGCardDetail.builder()
            .brand(card.getBrand())
            .last4(card.getLast4())
            .expMonth(card.getExpMonth().intValue())
            .expYear(card.getExpYear().intValue())
            .build();
      }),
  US_BANK_ACCOUNT(
      "us_bank_account",
      PaymentMethodType.BANK_ACCOUNT,
      pm -> {
        // TODO: Implement US_BANK_ACCOUNT support in the future.
        throw new UnsupportedOperationException(
            "[%s] BANK_ACCOUNT is not supported yet".formatted(PGProviderType.STRIPE));
      }),
  NZ_BANK_ACCOUNT(
      "nz_bank_account",
      PaymentMethodType.BANK_ACCOUNT,
      pm -> {
        // TODO: Implement NZ_BANK_ACCOUNT support in the future.
        throw new UnsupportedOperationException(
            "[%s] BANK_ACCOUNT is not supported yet".formatted(PGProviderType.STRIPE));
      });

  private final String typeName;
  private final PaymentMethodType domainMethodType;
  private final Function<PaymentMethod, ExtPGPaymentMethodDetail> converter;

  // Only CARD is supported for demo in Stripe G/W.
  private static final EnumSet<StripePaymentMethodType> STRIPE_SUPPORTIVE_METHODS =
      EnumSet.of(StripePaymentMethodType.CARD);

  public static boolean isSupportiveMethod(String method) {
    return STRIPE_SUPPORTIVE_METHODS.stream().anyMatch(type -> type.typeName.equals(method));
  }

  public static StripePaymentMethodType fromTypeName(String typeName) {
    if (!isSupportiveMethod(typeName)) {
      throw new IllegalArgumentException("Unsupported Stripe payment method: " + typeName);
    }

    return STRIPE_SUPPORTIVE_METHODS.stream()
        .filter(type -> type.typeName.equals(typeName))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Unsupported Stripe payment method: " + typeName));
  }
}
