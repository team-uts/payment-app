package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.common.annotation.DomainModel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a bank account payment method detail.
 *
 * <p>TODO (Not implemented for this demo, but can be used for future expansion)
 */
@DomainModel
@SuperBuilder
@Getter
public class BankAccount extends PaymentMethodDetail {
  private Long id;
  private Long memberId;
  private String bankName;
  private String accountNumber;
  private String accountHolderName;
}
