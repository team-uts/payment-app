package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.port.PaymentTransactionStorePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.entity.PaymentTransactionJpaEntity;
import dev.teamuts.payment.persistence.payment.mapper.PaymentTransactionConverter;
import dev.teamuts.payment.persistence.payment.repository.PaymentTransactionRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class PaymentTransactionStoreAdapter implements PaymentTransactionStorePort {
  private final PaymentTransactionRepository paymentTransactionRepository;
  private final PaymentTransactionConverter paymentTransactionConverter;

  @Override
  public List<PaymentTransaction> storeAll(List<PaymentTransaction> transactions) {
    if (transactions.isEmpty()) {
      return Collections.emptyList();
    }

    List<PaymentTransactionJpaEntity> entities =
        transactions.stream().map(paymentTransactionConverter::convertToJpaEntity).toList();

    List<PaymentTransactionJpaEntity> savedEntities =
        paymentTransactionRepository.saveAll(entities);

    return savedEntities.stream().map(paymentTransactionConverter::convertToDomainModel).toList();
  }
}
