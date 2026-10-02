package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.facade.pm.PaymentMethodDetailProcessorProvider;
import dev.teamuts.payment.domain.payment.model.BankAccount;
import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentMethodDetail;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMethodFacade {
  private final PaymentMethodService paymentMethodService;
  private final PaymentMethodDetailProcessorProvider pmDetailProcessorProvider;

  /**
   * Registers a new PaymentMethod and new PaymentMethodDetail ({@link Card}, {@link BankAccount}
   * ...) in the database based on {@link PaymentMethodType}.
   *
   * @param extPGPaymentMethod PaymentMethod from PG with its detail information
   * @return PaymentMethod registered in the database with its corresponding Card or BankAccount...
   */
  @AppTransactional
  public PaymentMethod registerPaymentMethodWithDetail(ExtPGPaymentMethodDto extPGPaymentMethod) {
    PaymentMethod storedPaymentMethod =
        paymentMethodService.registerNewPaymentMethod(extPGPaymentMethod);

    // Register the PaymentMethod Detail based on the method type (e.g., CARD, BANK_ACCOUNT, etc.)
    PaymentMethodDetail paymentMethodDetail =
        pmDetailProcessorProvider
            .getInstance(extPGPaymentMethod.getDetail().methodType())
            .registerPaymentMethodDetail(storedPaymentMethod, extPGPaymentMethod.getDetail());

    return PaymentMethod.copyWithDetail(storedPaymentMethod, paymentMethodDetail);
  }
}
