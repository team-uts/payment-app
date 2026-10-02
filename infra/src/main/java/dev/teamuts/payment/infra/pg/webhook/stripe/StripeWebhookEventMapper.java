package dev.teamuts.payment.infra.pg.webhook.stripe;

import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import dev.teamuts.payment.infra.pg.dto.StripeWebhookPayload;
import dev.teamuts.payment.shared.provider.ProviderService;

public abstract class StripeWebhookEventMapper implements ProviderService<StripeWebhookEventType> {
  public abstract StripeWebhookPayload convert(Event event);

  // Deserialize the event data to a StripeObject
  protected StripeObject deserializeEvent(Event event) {
    return event
        .getDataObjectDeserializer()
        .getObject()
        .orElseThrow(
            () ->
                new RuntimeException(
                    "[%s] Webhook event deserialization failed".formatted(PGProviderType.STRIPE)));
  }
}
