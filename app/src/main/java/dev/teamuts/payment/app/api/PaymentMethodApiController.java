package dev.teamuts.payment.app.api;

import dev.teamuts.payment.domain.payment.usecase.SetupPaymentMethodRegistrationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodApiController {
  private final SetupPaymentMethodRegistrationUseCase setupPaymentMethodUseCase;

  @PostMapping("/setup")
  public ResponseEntity<String> setupPaymentMethod() {
    setupPaymentMethodUseCase.execute();
    return ResponseEntity.ok("");
  }
}
