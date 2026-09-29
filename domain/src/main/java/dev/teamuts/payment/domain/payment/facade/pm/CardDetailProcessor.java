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
  private final CardService cardService;

  @Override
  public boolean supports(PaymentMethodType key) {
    return key == PaymentMethodType.CARD;
  }

  @Override
  @AppTransactional
  public PaymentMethodDetail registerPaymentMethodDetail(
      PaymentMethod paymentMethod, ExtPGPaymentMethodDetail detail) {
    // TODO: Implement card detail registration logic

    return null;
  }
}
