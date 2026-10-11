package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentTransactionReaderPort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.mapper.PaymentTransactionConverter;
import dev.teamuts.payment.persistence.payment.repository.PaymentTransactionRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional(readOnly = true)
public class PaymentTransactionReaderAdapter implements PaymentTransactionReaderPort {
  private final PaymentTransactionRepository paymentTransactionRepository;
  private final PaymentTransactionConverter paymentTransactionConverter;

  @Override
  public List<PaymentTransaction> retrieveAllByPaymentId(Long paymentId) {
    return paymentTransactionRepository.findAllByPaymentId(paymentId).stream()
        .map(paymentTransactionConverter::convertToDomainModel)
        .toList();
  }
}
