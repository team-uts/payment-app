package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.facade.PGWebhookEventProcessorProvider;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.service.PGExternalRequestService;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ProcessPGWebhookEventUseCase {
  private final PGExternalRequestService pgExternalRequestService;
  private final PGWebhookEventProcessorProvider webhookEventProcessorProvider;

  /**
   * {@link AppTransactional} should not be declared here.
   *
   * <p>In MySQL which has MVCC feature and default isolation level of REPEATABLE_READ, one tx will
   * read the data commited before the tx started.
   *
   * <p>That means if outer tx creates new webhook PGExternalRequest through inner separate tx, the
   * outer tx will not read the new record when processing the association. (Even though the inner
   * tx is committed)
   *
   * <p>Therefore, the entire process should be executed within a single transaction to ensure data
   * consistency.
   */
  public void execute(ExtPGWebhookEventDto event) {
    // register new PGExternalRequest for the webhook event
    PGExternalRequest webhookRequest = pgExternalRequestService.registerInitWebhookRequest(event);

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
