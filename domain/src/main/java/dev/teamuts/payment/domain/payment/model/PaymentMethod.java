package dev.teamuts.payment.domain.payment.model;

import dev.teamuts.payment.domain.payment.constant.PaymentMethodStatus;
import dev.teamuts.payment.domain.payment.constant.PaymentMethodType;
import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.dto.infra.ExtPGPaymentMethodDto;
import dev.teamuts.shared.domain.annotation.DomainModel;
import dev.teamuts.shared.domain.constant.ModelSourceType;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@DomainModel
@Getter
public class PaymentMethod extends BaseIdentifiableModel<Long> {
  private Long memberId;
  private PgProviderType pgProvider;
  private String providerToken;
  private PaymentMethodType methodType;
  private Boolean defaultMethod;
  private PaymentMethodStatus status;

  @Getter(AccessLevel.NONE)
  private PaymentMethodDetail detail;

  @Builder(access = AccessLevel.PRIVATE, toBuilder = true)
  private PaymentMethod(
      @Builder.ObtainVia(method = "getId") Long id,
      @Builder.ObtainVia(method = "getSourceType") ModelSourceType sourceType,
      Long memberId,
      PgProviderType pgProvider,
      String providerToken,
      PaymentMethodType methodType,
      Boolean defaultMethod,
      PaymentMethodStatus status,
      PaymentMethodDetail detail) {
    super(id, sourceType);
    this.memberId = memberId;
    this.pgProvider = pgProvider;
    this.providerToken = providerToken;
    this.methodType = methodType;
    this.defaultMethod = defaultMethod;
    this.status = status;
    this.detail = detail;
  }

  public static PaymentMethod newActiveMethod(ExtPGPaymentMethodDto extPGPaymentMethod) {
    PaymentMethodBuilder builder =
        PaymentMethod.builder()
            .sourceType(ModelSourceType.LOGIC)
            .memberId(extPGPaymentMethod.getMemberId())
            .pgProvider(extPGPaymentMethod.getPgProvider())
            .providerToken(extPGPaymentMethod.getPgPaymentMethodId())
            .methodType(extPGPaymentMethod.getDetail().methodType())
            .defaultMethod(true) // TODO: Set defaultMethod based on business logic
            .status(PaymentMethodStatus.ACTIVE);

    return builder.build();
  }

  public static PaymentMethod point(Long memberId) {
    return PaymentMethod.builder()
        .sourceType(ModelSourceType.LOGIC)
        .memberId(memberId)
        .pgProvider(PgProviderType.NONE)
        .providerToken(null)
        .methodType(PaymentMethodType.POINT)
        .defaultMethod(false)
        .status(PaymentMethodStatus.ACTIVE)
        .build();
  }

  public static PaymentMethod fromDatabase(
      Long id,
      Long memberId,
      PgProviderType pgProvider,
      String providerToken,
      PaymentMethodType methodType,
      Boolean defaultMethod,
      PaymentMethodStatus status,
      PaymentMethodDetail detail) {
    return PaymentMethod.builder()
        .id(id)
        .sourceType(ModelSourceType.DATABASE)
        .memberId(memberId)
        .pgProvider(pgProvider)
        .providerToken(providerToken)
        .methodType(methodType)
        .defaultMethod(defaultMethod)
        .status(status)
        .detail(detail)
        .build();
  }

  public void updateDetail(PaymentMethodDetail newDetail) {
    this.detail = newDetail;
  }

  // Return copy
  public PaymentMethodDetail getDetailOrNull() {
    if (detail instanceof Card cardDetail) {
      return cardDetail.copy();
    }

    return null;
  }

  public void checkIfBelongsToMember(Long memberId) {
    if (!this.memberId.equals(memberId)) {
      throw new IllegalArgumentException(
          "PaymentMethod does not belong to the member (memberId: %s)".formatted(memberId));
    }
  }

  public void checkIfActive() {
    if (this.status != PaymentMethodStatus.ACTIVE) {
      throw new IllegalStateException(
          "PaymentMethod (id: %s) is not active".formatted(this.getId()));
    }
  }

  public boolean isPointType() {
    return this.methodType == PaymentMethodType.POINT;
  }
}
