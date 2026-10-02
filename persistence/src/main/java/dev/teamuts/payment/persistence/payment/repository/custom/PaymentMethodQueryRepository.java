package dev.teamuts.payment.persistence.payment.repository.custom;

import dev.teamuts.payment.persistence.payment.dto.PaymentMethodListRow;
import java.util.List;

public interface PaymentMethodQueryRepository {
  List<PaymentMethodListRow> findListByMemberIdJoin(Long memberId);
}
