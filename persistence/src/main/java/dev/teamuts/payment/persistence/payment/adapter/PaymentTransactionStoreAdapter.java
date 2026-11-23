package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.Payment;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.model.PaymentTransactions;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentTransactionStorePort;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import dev.teamuts.payment.persistence.payment.entity.PaymentTransactionJpaEntity;
import dev.teamuts.payment.persistence.payment.mapper.PaymentTransactionConverter;
import dev.teamuts.payment.persistence.payment.repository.PaymentRepository;
import dev.teamuts.payment.persistence.payment.repository.PaymentTransactionRepository;
import dev.teamuts.shared.data.PersistenceException;
import dev.teamuts.shared.data.annotation.AppTransactional;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class PaymentTransactionStoreAdapter implements PaymentTransactionStorePort {
  private final PaymentTransactionRepository paymentTransactionRepository;
  private final PaymentRepository paymentRepository;
  private final PaymentTransactionConverter paymentTransactionConverter;

  @Override
  public PaymentTransactions storeAll(PaymentTransactions transactions) {
    if (transactions.isEmpty()) {
      return PaymentTransactions.empty();
    }

    Long paymentId = transactions.getCommonPaymentId();
    PaymentJpaEntity paymentEntity =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> PersistenceException.notFound(Payment.class, paymentId));

    List<PaymentTransactionJpaEntity> entities =
        transactions.mapToList(transaction ->
            paymentTransactionConverter.convertToJpaEntity(transaction, paymentEntity));

    List<PaymentTransactionJpaEntity> savedEntities =
        paymentTransactionRepository.saveAll(entities);

    List<PaymentTransaction> savedTxs =
        savedEntities.stream().map(paymentTransactionConverter::convertToDomainModel).toList();

    return PaymentTransactions.of(savedTxs);
  }
}
