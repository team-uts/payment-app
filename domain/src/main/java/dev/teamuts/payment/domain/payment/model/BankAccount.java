package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.constant.ModelSourceType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents a bank account payment method detail.
 *
 * <p>TODO (Not implemented for this demo, but can be used for future expansion)
 */
@DomainModel
@Getter
public class BankAccount extends PaymentMethodDetail {
  private Long memberId;
  private String bankName;
  private String accountNumber;
  private String accountHolderName;

  @Builder(access = AccessLevel.PRIVATE, toBuilder = true)
  private BankAccount(
      @Builder.ObtainVia(method = "getId") Long id,
      @Builder.ObtainVia(method = "getSourceType") ModelSourceType sourceType,
      Long payMethodId,
      Long memberId,
      String bankName,
      String accountNumber,
      String accountHolderName) {
    super(id, sourceType, payMethodId);
    this.memberId = memberId;
    this.bankName = bankName;
    this.accountNumber = accountNumber;
    this.accountHolderName = accountHolderName;
  }
}
