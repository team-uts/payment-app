package dev.teamuts.payment.persistence.payment.repository.custom;

import static dev.teamuts.payment.persistence.payment.entity.QCardJpaEntity.cardJpaEntity;
import static dev.teamuts.payment.persistence.payment.entity.QPaymentMethodJpaEntity.paymentMethodJpaEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.teamuts.payment.persistence.payment.dto.PaymentMethodListRow;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentMethodQueryRepositoryImpl implements PaymentMethodQueryRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<PaymentMethodListRow> findListByMemberIdJoin(Long memberId) {
    return jpaQueryFactory
        .select(
            Projections.constructor(
                PaymentMethodListRow.class,
                paymentMethodJpaEntity.id,
                paymentMethodJpaEntity.memberId,
                paymentMethodJpaEntity.pgProvider,
                paymentMethodJpaEntity.methodType,
                paymentMethodJpaEntity.defaultMethod,
                paymentMethodJpaEntity.status,
                cardJpaEntity.id,
                cardJpaEntity.memberId,
                cardJpaEntity.brand,
                cardJpaEntity.lastFour,
                cardJpaEntity.expiryMonth,
                cardJpaEntity.expiryYear,
                cardJpaEntity.status))
        .from(paymentMethodJpaEntity)
        .leftJoin(cardJpaEntity)
        .on(cardJpaEntity.payMethodId.eq(paymentMethodJpaEntity.id))
        .where(paymentMethodJpaEntity.memberId.eq(memberId))
        .fetch();
  }
}
