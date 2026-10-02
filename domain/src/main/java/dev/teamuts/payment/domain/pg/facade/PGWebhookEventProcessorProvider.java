package dev.teamuts.payment.domain.pg.facade;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PGWebhookEventProcessorProvider
    extends Provider<PGRequestType, BasePGWebhookEventProcessor> {

  protected PGWebhookEventProcessorProvider(List<BasePGWebhookEventProcessor> services) {
    super(services);
  }
}
