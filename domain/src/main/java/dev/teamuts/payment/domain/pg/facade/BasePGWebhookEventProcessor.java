package dev.teamuts.payment.domain.pg.facade;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.shared.provider.ProviderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasePGWebhookEventProcessor implements ProviderService<PGRequestType> {
  public boolean processEvent(ExtPGWebhookEventDto event) {
    // If the event is not succeeded, just return true and leave this processor
    // to complete the PGExternalRequest
    if (!event.isSucceeded()) {
      return true;
    }

    try {
      process(event);
    } catch (Exception e) {
      log.error("Error processing webhook event: {}", e.getMessage(), e);
      return false;
    }

    return true;
  }

  protected abstract void process(ExtPGWebhookEventDto event);
}
