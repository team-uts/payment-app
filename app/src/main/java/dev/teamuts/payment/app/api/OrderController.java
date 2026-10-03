package dev.teamuts.payment.app.api;

import dev.teamuts.payment.app.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API", description = "Order Domain")
@RestController
@RequestMapping("/v1/orders")
public class OrderController {
  @PostMapping("/create")
  public ApiResponse<String> createOrder() {
    return ApiResponse.success("Order created successfully");
  }
}
