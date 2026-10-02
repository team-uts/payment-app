package dev.teamuts.payment.domain.payment.facade.pm;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentMethodDetail;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto.ExtPGPaymentMethodDetail;
import dev.teamuts.payment.shared.provider.ProviderService;

public interface PaymentMethodDetailProcessor extends ProviderService<PaymentMethodType> {
  PaymentMethodDetail registerPaymentMethodDetail(
      PaymentMethod paymentMethod, ExtPGPaymentMethodDetail detail);
}
