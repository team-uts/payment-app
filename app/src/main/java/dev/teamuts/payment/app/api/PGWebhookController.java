package dev.teamuts.payment.app.api;

import dev.teamuts.payment.app.common.response.ApiResponse;
import dev.teamuts.payment.app.pg.PGWebhookPayload;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.PGExtOperationInfo.WebhookEventInfo;
import dev.teamuts.payment.domain.pg.usecase.ProcessPGWebhookEventUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "Payment Gateway Webhook",
    description = "For webhook events from payment gateway providers")
@RestController
@RequestMapping("/v1/webhook")
@RequiredArgsConstructor
public class PGWebhookController {
  private final ProcessPGWebhookEventUseCase processPGWebhookEventUseCase;

  @Operation(
      summary = "Receive Webhook Event",
      description = "Processes webhook events received from various payment gateway providers.")
  @PostMapping("/payment-methods")
  public ApiResponse<String> processWebhookEvent(
      @PGWebhookPayload(
              requestType = PGRequestType.PAYMENT_METHOD,
              pgProvider = PGProviderType.STRIPE)
          WebhookEventInfo event) {
    processPGWebhookEventUseCase.execute(event);

    return ApiResponse.success("ok");
  }
}
