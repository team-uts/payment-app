package dev.teamuts.payment.infra.pg.webhook.stripe;

import com.stripe.model.Event;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeObject;
import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import dev.teamuts.payment.infra.pg.dto.StripeWebhookPayload;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class SetupIntentWebhookEventMapper extends StripeWebhookEventMapper {
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
    StripeObject stripeObject = super.deserializeEvent(event);
    StripeWebhookEventType eventType = StripeWebhookEventType.fromEventTypeName(event.getType());

    SetupIntent setupIntent = (SetupIntent) stripeObject;

    // retrieve metadata according to the ExtPGOperationType metadata key
    String metadataValue =
        setupIntent.getMetadata().get(eventType.getOperationType().getMetadataKey());

    // TODO: Check whether the card information can be retrieved here from
    //  setupIntent.getPaymentMethodObject()
    return StripeWebhookPayload.builder()
        .memberId(metadataValue != null ? Long.parseLong(metadataValue) : null)
        .pgOperationId(setupIntent.getId())
        .eventType(eventType)
        .pgProviderTokenId(setupIntent.getPaymentMethod())
        .build();
  }
}
