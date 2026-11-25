package dev.teamuts.payment.domain.payment.constant;

import static dev.teamuts.payment.domain.order.constant.AppServiceType.*;

import dev.teamuts.payment.domain.order.constant.AppServiceType;
import java.util.Map;
import lombok.Getter;

/**
 * Each {@link AppServiceType} could have multiple {@link PaymentType}.<br>
 * (e.g., {@link AppServiceType#TAXI}
 */
@Getter
public enum PaymentType {
  TAXI_FARE(TAXI),
  BUSINESS_TAXI_FARE(TAXI),
  DELIVERY_FEE(DELIVERY),
  FOOD_DELIVERY_FEE(FOOD_DELIVERY),
  ;

  private final AppServiceType serviceType;

  PaymentType(AppServiceType serviceType) {
    this.serviceType = serviceType;
  }

  private static final Map<AppServiceType, PaymentType> PAYMENT_TYPE_MAP_FROM_CUSTOMER =
      Map.of(
          TAXI, TAXI_FARE,
          DELIVERY, DELIVERY_FEE,
          FOOD_DELIVERY, FOOD_DELIVERY_FEE);

  public static PaymentType fromCustomerServiceType(AppServiceType serviceType) {
    return PAYMENT_TYPE_MAP_FROM_CUSTOMER.get(serviceType);
  }
}
