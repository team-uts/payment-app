package dev.teamuts.payment.infra.pg.webhook.stripe;

import com.stripe.model.Event;
import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import dev.teamuts.payment.infra.pg.dto.StripeWebhookPayload;
import dev.teamuts.payment.shared.provider.ProviderService;

public interface StripeWebhookEventMapper extends ProviderService<StripeWebhookEventType> {
  StripeWebhookPayload convert(Event event);
}
