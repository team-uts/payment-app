package dev.teamuts.payment.infra.pg.webhook.stripe;

import com.stripe.model.Event;
import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import dev.teamuts.payment.infra.pg.dto.StripeWebhookPayload;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class SetupIntentWebhookEventMapper implements StripeWebhookEventMapper {
  private static final Set<StripeWebhookEventType> SUPPORTED_EVENT_TYPES =
      Set.of(
          StripeWebhookEventType.SETUP_INTENT_SUCCEEDED,
          StripeWebhookEventType.SETUP_INTENT_SETUP_FAILED);

  @Override
  public boolean supports(StripeWebhookEventType key) {
    return SUPPORTED_EVENT_TYPES.contains(key);
  }

  @Override
  public StripeWebhookPayload convert(Event event) {
    // TODO: Implement the conversion logic
    return null;
  }
}
