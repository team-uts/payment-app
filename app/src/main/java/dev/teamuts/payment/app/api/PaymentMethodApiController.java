package dev.teamuts.payment.app.api;

import static dev.teamuts.payment.domain.pg.dto.PGExtRequestInfo.*;

import dev.teamuts.payment.app.api.dto.PaymentMethodDtoMapper;
import dev.teamuts.payment.app.api.dto.SetupPaymentMethodRequestDto;
import dev.teamuts.payment.app.common.response.ApiResponse;
import dev.teamuts.payment.domain.paymentmethod.dto.SetupPaymentMethodCommand;
import dev.teamuts.payment.domain.paymentmethod.usecase.SetupPaymentMethodRegistrationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment Method API", description = "Payment Method Domain")
@RestController
@RequestMapping("/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodApiController {
  private final SetupPaymentMethodRegistrationUseCase setupPaymentMethodUseCase;
  private final PaymentMethodDtoMapper mapper;

  @Operation(
      summary = "Initialize Payment Method Setup",
      description = "Initialize payment method setup with PG and store the request in database")
  @PostMapping("/setup")
  public ApiResponse<SetupPaymentMethodInfo> setupPaymentMethod(
      @RequestBody SetupPaymentMethodRequestDto request) {
    SetupPaymentMethodCommand command = mapper.of(request);

    SetupPaymentMethodInfo result = setupPaymentMethodUseCase.execute(command);

    return ApiResponse.success(result);
  }
}
