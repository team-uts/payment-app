package dev.teamuts.payment.domain.pg.facade;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PGWebhookEventProcessorProvider
    extends Provider<PGRequestType, AbstractPGWebhookEventProcessor> {

  protected PGWebhookEventProcessorProvider(List<AbstractPGWebhookEventProcessor> services) {
    super(services);
  }
}
