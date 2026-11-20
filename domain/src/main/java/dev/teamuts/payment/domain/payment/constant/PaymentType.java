package dev.teamuts.payment.domain.payment.constant;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import java.util.Map;
import lombok.Getter;

/**
 * Each {@link AppServiceType} could have multiple {@link PaymentType}.<br>
 * (e.g., {@link AppServiceType#TAXI}
 */
@Getter
public enum PaymentType {
  TAXI_FARE(AppServiceType.TAXI),
  BUSINESS_TAXI_FARE(AppServiceType.TAXI),
  DELIVERY_FEE(AppServiceType.DELIVERY),
  FOOD_DELIVERY_FEE(AppServiceType.FOOD_DELIVERY),
  ;

  private final AppServiceType serviceType;

  PaymentType(AppServiceType serviceType) {
    this.serviceType = serviceType;
  }

  private static final Map<AppServiceType, PaymentType> PAYMENT_TYPE_MAP_FROM_CUSTOMER =
      Map.of(
          AppServiceType.TAXI, TAXI_FARE,
          AppServiceType.DELIVERY, DELIVERY_FEE,
          AppServiceType.FOOD_DELIVERY, FOOD_DELIVERY_FEE);

  public static PaymentType fromCustomerServiceType(AppServiceType serviceType) {
    return PAYMENT_TYPE_MAP_FROM_CUSTOMER.get(serviceType);
  }
}
