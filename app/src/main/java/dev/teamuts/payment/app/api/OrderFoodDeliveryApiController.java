package dev.teamuts.payment.app.api;

import dev.teamuts.payment.app.api.dto.OrderDtoMapper;
import dev.teamuts.payment.app.api.dto.PayOrderRequestDto;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.dto.PayOrderInfo;
import dev.teamuts.payment.domain.order.usecase.PayOrderUseCase;
import dev.teamuts.payment.domain.payment.constant.PaymentType;
import dev.teamuts.shared.http.response.ApiCommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Order API for Food Delivery Service",
    description = "Order Domain in terms of Food Delivery Service")
@RestController
@RequestMapping("/v1/orders/food-delivery")
@RequiredArgsConstructor
public class OrderFoodDeliveryApiController {
  private final OrderDtoMapper mapper;
  private final PayOrderUseCase payOrderUseCase;

  private static final AppServiceType SERVICE_TYPE = AppServiceType.FOOD_DELIVERY;

  @Operation(summary = "Pay Order", description = "Pay Order API (External PG, Point)")
  @PostMapping("/pay")
  public ApiCommonResponse<PayOrderInfo> payOrder(
      @RequestParam Long memberId, @RequestBody PayOrderRequestDto request) {
    var command = mapper.of(request);

    PayOrderInfo result =
        payOrderUseCase.execute(
            memberId, command, PaymentType.fromCustomerServiceType(SERVICE_TYPE));

    return ApiCommonResponse.success(result);
  }
}
