package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentMethodReaderPort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.dto.PaymentMethodListRow;
import dev.teamuts.payment.persistence.payment.mapper.PaymentMethodConverter;
import dev.teamuts.payment.persistence.payment.repository.PaymentMethodRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentMethodReaderAdapter implements PaymentMethodReaderPort {
  private final PaymentMethodRepository paymentMethodRepository;
  private final PaymentMethodConverter paymentMethodConverter;

  @Override
  public List<PaymentMethod> retrievePaymentMethodsByMemberId(Long memberId) {
    List<PaymentMethodListRow> rows = paymentMethodRepository.findListByMemberIdJoin(memberId);

    return rows.stream().map(paymentMethodConverter::convertToDomainModel).toList();
  }
}
