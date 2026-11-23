package dev.teamuts.payment.app.api;

import dev.teamuts.payment.app.api.dto.OrderDtoMapper;
import dev.teamuts.payment.app.api.dto.PayOrderRequestDto;
import dev.teamuts.payment.domain.order.constant.AppServiceType;
import dev.teamuts.payment.domain.order.dto.PayOrderInfo;
import dev.teamuts.payment.domain.order.usecase.PayOrderUseCase;
import dev.teamuts.shared.http.response.ApiCommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order API for Taxi Service", description = "Order Domain in terms of Taxi Service")
@RestController
@RequestMapping("/v1/orders/taxi")
@RequiredArgsConstructor
public class OrderTaxiApiController {
  private final OrderDtoMapper mapper;
  private final PayOrderUseCase payOrderUseCase;

  private static final AppServiceType SERVICE_TYPE = AppServiceType.TAXI;

  @Operation(summary = "Pay Order Taxi", description = "Pay Order Taxi API (External PG, Point)")
  @PostMapping("/pay")
  public ApiCommonResponse<PayOrderInfo> payOrder(
      @RequestParam Long memberId, @RequestBody PayOrderRequestDto request) {
    var command = mapper.of(request, SERVICE_TYPE);

    PayOrderInfo result = payOrderUseCase.execute(memberId, command);

    return ApiCommonResponse.success(result);
  }
}
