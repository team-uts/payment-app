package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.PaymentMethod;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentMethodReaderPort;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.dto.PaymentMethodListRow;
import dev.teamuts.payment.persistence.payment.entity.PaymentMethodJpaEntity;
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

  @Override
  public PaymentMethod retrievePaymentMethodById(Long id) {
    PaymentMethodJpaEntity entity =
        paymentMethodRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Payment method not found with id: " + id));

    return paymentMethodConverter.convertToDomainModel(entity);
  }

  @Override
  public boolean existsByParameters(
      Long memberId, String pgProviderToken, PGProviderType pgProvider) {
    return paymentMethodRepository.existsByMemberIdAndProviderTokenAndPgProvider(
        memberId, pgProviderToken, pgProvider);
  }
}
