package dev.teamuts.payment.app.api;

import dev.teamuts.payment.app.pg.PGWebhookPayload;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/webhook/stripe")
public class StripeWebhookController {
  @PostMapping("/payment-methods")
  public String stripeWebhook(
      @PGWebhookPayload(
              requestType = PGRequestType.PAYMENT_METHOD,
              pgProvider = PGProviderType.STRIPE)
          String payload) {
    return "ok";
  }
}
