package dev.teamuts.payment.domain.payment.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.payment.dto.PaymentMethodInfo;
import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.service.PaymentMethodService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPaymentMethodListUseCase {
  private final PaymentMethodService paymentMethodService;

  public List<PaymentMethodInfo> execute(Long memberId) {
    List<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethodList(memberId);

    return paymentMethods.stream().map(PaymentMethodInfo::of).toList();
  }
}
