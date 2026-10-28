package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents a bank account payment method detail.
 *
 * <p>TODO (Not implemented for this demo, but can be used for future expansion)
 */
@DomainModel
@Builder(access = lombok.AccessLevel.PRIVATE, toBuilder = true)
@Getter
public class BankAccount extends PaymentMethodDetail {
  private Long memberId;
  private String bankName;
  private String accountNumber;
  private String accountHolderName;
}
