package dev.teamuts.payment.domain.pg.dto;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtPGPaymentMethodDto {
  private Long memberId;
  private String pgPaymentMethodId;
  private PGProviderType pgProvider;
  private ExtPGPaymentMethodDetail detail;

  public sealed interface ExtPGPaymentMethodDetail {
    PaymentMethodType methodType();
  }

  /**
   * @see <a href="https://docs.stripe.com/api/cards/object">Stripe Card object API</a>
   */
  @Getter
  @Builder
  public static final class ExtPGCardDetail implements ExtPGPaymentMethodDetail {
    private String brand;
    private String last4;
    private Integer expMonth;
    private Integer expYear;

    @Override
    public PaymentMethodType methodType() {
      return PaymentMethodType.CARD;
    }
  }

  /**
   * TODO: Add more fields for bank account details if needed.
   *
   * @see <a href="https://docs.stripe.com/api/customer_bank_accounts/object">Stripe Bank Account
   *     object API</a>
   */
  @Getter
  @Builder
  public static final class ExtPGBankAccountDetail implements ExtPGPaymentMethodDetail {
    private String bankName;
    private String accountNumber;
    private String routingNumber;

    @Override
    public PaymentMethodType methodType() {
      return PaymentMethodType.BANK_ACCOUNT;
    }
  }
}
