package dev.teamuts.payment.domain.pg.facade;

import static dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType.*;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.shared.provider.ProviderService;
import lombok.extern.slf4j.Slf4j;

/**
 * They can have {@link PGWebhookEventProcessResultType#PENDING} result. For example, the network
 * issue happens when requesting to PG API, we can return PENDING to let the PGExternalRequest to
 * retry later.
 *
 * <p>The PENDING status is not supported at the moment.
 */
@Slf4j
public abstract class BasePGWebhookEventProcessor implements ProviderService<PGRequestType> {
  public PGWebhookEventProcessResultType processEvent(ExtPGWebhookEventDto event) {
    // If the event is not succeeded, just return true and leave this processor
    // to complete the PGExternalRequest
    if (!event.isSucceeded()) {
      return SUCCESS;
    }

    try {
      process(event);
    } catch (Exception e) {
      log.error("Error processing webhook event: {}", e.getMessage(), e);
      return FAILURE;
    }

    return SUCCESS;
  }

  protected abstract void process(ExtPGWebhookEventDto event);
}
