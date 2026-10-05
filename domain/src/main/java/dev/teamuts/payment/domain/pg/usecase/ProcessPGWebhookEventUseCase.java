package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.facade.PGExternalRequestFacade;
import dev.teamuts.payment.domain.pg.facade.PGWebhookEventProcessorProvider;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.service.PGExternalRequestService;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ProcessPGWebhookEventUseCase {
  private final PGExternalRequestService pgExternalRequestService;
  private final PGExternalRequestFacade pgExternalRequestFacade;
  private final PGWebhookEventProcessorProvider webhookEventProcessorProvider;

  /**
   * {@link AppTransactional} should not be declared here.
   *
   * <p>In MySQL which has MVCC feature and default isolation level of REPEATABLE_READ, one tx will
   * read the data commited before the tx started.
   *
   * <p>That means if outer tx creates new webhook PGExternalRequest through inner separate tx, the
   * outer tx will not read the new record when processing the association. (Even though the inner
   * tx is committed). Therefore, the entire process should be executed within a separate
   * transaction to ensure data consistency.
   */
  public void execute(ExtPGWebhookEventDto event) {
    // If the event is not succeeded, just return and leave this processor.
    if (event.isNotSucceeded()) {
      log.info("Webhook event is not succeeded. Skipped ({})", event.getBaseInfoForLog());
      return;
    }

    // register new PGExternalRequest for the webhook event
    // Be careful here that REPEATABLE_READ isolation level of MySQL could cause issues.
    PGExternalRequest webhookRequest =
        pgExternalRequestFacade.getInitWebhookRequestOrRegisterInitWebhookRequest(event);

    // For idempotency, skip processing the event if the webhook is already completed
    if (webhookRequest.isCompleted()) {
      log.info("Webhook event is already processed. Skipped ({})", event.getBaseInfoForLog());
      return;
    }

    // process the event data based on the pg webhook type when the event is succeeded
    PGWebhookEventProcessResultType processResultType =
        webhookEventProcessorProvider.getInstance(event.getRequestType()).processEvent(event);

    // retrieve base PGExternalRequest of webhook event
    PGExternalRequest sourceRequest =
        pgExternalRequestService.getPGExternalRequest(
            event.getPgProvider(),
            event.getMemberId(),
            event.getPgRequestId(),
            event.getPgBaseOperationName());

    // associate the new PGExternalRequest with the source PGExternalRequest
    // and update the status of the webhook PGExternalRequest
    pgExternalRequestService.associateWithSourceAfterProcessWebhook(
        webhookRequest, sourceRequest, processResultType);

    // update the base PGExternalRequest status to COMPLETED
    pgExternalRequestService.completeSourceRequest(sourceRequest);
  }
}
