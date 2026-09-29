package dev.teamuts.payment.domain.payment.facade;

import dev.teamuts.payment.domain.payment.facade.pm.PaymentMethodDetailProcessorProvider;
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

  @AppTransactional
  public PaymentMethod registerPaymentMethodWithDetail(ExtPGPaymentMethodDto extPGPaymentMethod) {
    PaymentMethod storedPaymentMethod =
        paymentMethodService.registerNewPaymentMethod(extPGPaymentMethod);

    PaymentMethodDetail paymentMethodDetail =
        pmDetailProcessorProvider
            .getInstance(extPGPaymentMethod.getDetail().methodType())
            .registerPaymentMethodDetail(storedPaymentMethod, extPGPaymentMethod.getDetail());

    // TODO: Think about better way
    storedPaymentMethod.updateDetail(paymentMethodDetail);

    return storedPaymentMethod;
  }
}
