package dev.teamuts.payment.infra.pg.webhook.stripe;

import dev.teamuts.payment.infra.pg.constant.StripeWebhookEventType;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StripeWebhookEventMapperProvider
    extends Provider<StripeWebhookEventType, StripeWebhookEventMapper> {

  protected StripeWebhookEventMapperProvider(List<StripeWebhookEventMapper> services) {
    super(services);
  }
}
