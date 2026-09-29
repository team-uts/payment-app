package dev.teamuts.payment.domain.pg.port.infra;

import dev.teamuts.payment.domain.pg.constant.PGWebhookEventStatus;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;

/**
 * This interface defines mapping webhook event types to their corresponding operation names,
 * detailed messages, and statuses.
 *
 * <p>This information will be used to register new {@link PGExternalRequest} domain object.
 */
public interface PGWebhookEventTypeMappable {
  String getPGOperationName();

  String getPGDetailedMessage();

  PGWebhookEventStatus getWebhookEventStatus();
}
