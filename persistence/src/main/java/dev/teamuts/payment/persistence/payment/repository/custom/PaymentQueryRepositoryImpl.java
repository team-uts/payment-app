package dev.teamuts.payment.persistence.payment.repository.custom;

import static dev.teamuts.payment.persistence.payment.entity.QPaymentJpaEntity.paymentJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.teamuts.payment.persistence.payment.entity.PaymentJpaEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepositoryImpl implements PaymentQueryRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<PaymentJpaEntity> findAllByOrderIdJoiningTransactions(Long orderId) {
    return jpaQueryFactory
        .selectFrom(paymentJpaEntity)
        .leftJoin(paymentJpaEntity.transactions)
        .fetchJoin()
        .where(paymentJpaEntity.orderId.eq(orderId))
        .fetch();
  }
}
