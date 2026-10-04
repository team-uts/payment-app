package dev.teamuts.payment.app.api;

import dev.teamuts.payment.app.api.dto.OrderDtoMapper;
import dev.teamuts.payment.app.api.dto.PayOrderRequestDto;
import dev.teamuts.payment.app.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API", description = "Order Domain")
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderDtoMapper mapper;

  /**
   * External Service will call this API to process their orders with payment. This API will create
   * an Order record and request payment, but the order is different from the one from the external
   * service side (just for recording in the payment system to track).
   *
   * <p>TODO: member authentication should be added to this API
   */
  @PostMapping("/pay")
  public ApiResponse<String> payOrder(
      @RequestParam Long memberId, @RequestBody PayOrderRequestDto request) {
    var command = mapper.of(request);
    return ApiResponse.success("Order created successfully");
  }
}
