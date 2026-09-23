package dev.teamuts.payment.app.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/webhook/stripe")
public class StripeWebhookController {
  @PostMapping("/payment-methods")
  public String StripeWebhook(@RequestBody String payload) {
    return "ok";
  }
}
