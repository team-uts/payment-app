package dev.teamuts.payment.domain.payment.facade.pm;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.model.PaymentMethodDetail;
import dev.teamuts.payment.domain.payment.service.CardService;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodDto.ExtPGPaymentMethodDetail;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardDetailProcessor implements PaymentMethodDetailProcessor {
  private static final PaymentMethodType SUPPORTED_METHOD_TYPE = PaymentMethodType.CARD;
  private final CardService cardService;

  @Override
  public boolean supports(PaymentMethodType key) {
    return key == SUPPORTED_METHOD_TYPE;
  }

  @Override
  @AppTransactional
  public PaymentMethodDetail registerPaymentMethodDetail(
      PaymentMethod paymentMethod, ExtPGPaymentMethodDetail detail) {

    return cardService.registerNewCard(paymentMethod, detail);
  }
}
