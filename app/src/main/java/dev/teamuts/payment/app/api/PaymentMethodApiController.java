package dev.teamuts.payment.app.api;

import dev.teamuts.payment.app.api.dto.PaymentMethodDtoMapper;
import dev.teamuts.payment.app.api.dto.SetupPaymentMethodRequestDto;
import dev.teamuts.payment.domain.paymentmethod.dto.SetupPaymentMethodCommand;
import dev.teamuts.payment.domain.paymentmethod.usecase.SetupStripePaymentMethodRegistrationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodApiController {
  private final SetupStripePaymentMethodRegistrationUseCase setupPaymentMethodUseCase;
  private final PaymentMethodDtoMapper mapper;

  @PostMapping("/setup")
  public ResponseEntity<String> setupPaymentMethod(
      @RequestBody SetupPaymentMethodRequestDto request) {
    SetupPaymentMethodCommand command = mapper.of(request);

    String result = setupPaymentMethodUseCase.execute(command);

    return ResponseEntity.ok(result);
  }
}
