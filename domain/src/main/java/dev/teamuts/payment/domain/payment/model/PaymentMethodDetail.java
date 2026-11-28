package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.shared.domain.constant.ModelSourceType;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import lombok.Getter;

@Getter
public abstract class PaymentMethodDetail extends BaseIdentifiableModel<Long> {
  protected Long payMethodId;

  protected PaymentMethodDetail(Long id, ModelSourceType sourceType, Long payMethodId) {
    super(id, sourceType);
    this.payMethodId = payMethodId;
  }
}
