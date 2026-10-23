package dev.teamuts.payment.persistence.payment.adapter;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.payment.port.persistence.PaymentTransactionUpdatePort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.payment.entity.PaymentTransactionJpaEntity;
import dev.teamuts.payment.persistence.payment.repository.PaymentTransactionRepository;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
@AppTransactional
public class PaymentTransactionUpdateAdapter implements PaymentTransactionUpdatePort {
  private final PaymentTransactionRepository paymentTransactionRepository;

  @Override
  public void updatePGTransactionInfo(PaymentTransaction transaction) {
    PaymentTransactionJpaEntity entity =
        paymentTransactionRepository
            .findById(transaction.getId())
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "PaymentTransaction with ID " + transaction.getId() + " not found"));

    entity.updatePGTransactionInfo(
        transaction.getPgTransactionId(),
        transaction.getPgResponseCode(),
        transaction.getPgErrorCode(),
        transaction.getPgErrorMessage(),
        transaction.getDetailMessage(),
        transaction.getStatus());
  }

  @Override
  public void updateDetailMessage(PaymentTransaction transaction) {
    PaymentTransactionJpaEntity entity =
        paymentTransactionRepository
            .findById(transaction.getId())
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "PaymentTransaction with ID " + transaction.getId() + " not found"));

    entity.updateDetailMessage(transaction.getDetailMessage());
  }
}
