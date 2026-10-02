package dev.teamuts.payment.domain.pg.facade;

import static dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType.*;

import dev.teamuts.payment.domain.pg.constant.PGRequestType;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.shared.data.AppTransactional;
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

  /**
   * Consider {@link AppTransactional#propagation()}. This method should be executed in a separate
   * transaction to process this operation independently. (e.g., to do this operation separately
   * from the *PG webhook event* processing transaction)
   */
  @AppTransactional
  public PGWebhookEventProcessResultType processEvent(ExtPGWebhookEventDto event) {
    // If the event is not succeeded, just return true and leave this processor
    // to complete the PGExternalRequest
    if (event.isNotSucceeded()) {
      return SUCCESS;
    }

    if (checkAlreadyProcessed(event)) {
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

  /**
   * Default is false to process the event inside. The implementing class can override this to check
   * if the event has already been processed.
   */
  protected boolean checkAlreadyProcessed(ExtPGWebhookEventDto event) {
    return false;
  }

  /** Process the webhook event. (Main business logic here) */
  protected abstract void process(ExtPGWebhookEventDto event);
}
