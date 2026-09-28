package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.pg.constant.PGRequestStatus;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.facade.PGWebhookEventProcessorProvider;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.infra.provider.ExternalPGServiceProvider;
import dev.teamuts.payment.domain.pg.service.PGExternalRequestService;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ProcessPGWebhookEventUseCase {
  private final PGExternalRequestService pgExternalRequestService;
  private final ExternalPGServiceProvider pgServiceProvider;
  private final PGWebhookEventProcessorProvider webhookEventProcessorProvider;

  @AppTransactional
  public void execute(ExtPGWebhookEventDto event) {
    // retrieve PGExternalRequest List from DB based on the event data
    List<PGExternalRequest> pgExtRequests =
        pgExternalRequestService.getPGExternalRequestList(
            event.getPgProvider(), event.getMemberId(), event.getPgRequestId());

    // find a single proper data according to the event type
    PGExternalRequest basePGExtRequest =
        pgServiceProvider
            .getInstance(event.getPgProvider())
            .findBaseExternalRequestForWebhook(pgExtRequests, event.getRequestType());

    // process the event data based on the pg webhook type when the event is succeeded
    boolean isProcessSuccess =
        webhookEventProcessorProvider.getInstance(event.getRequestType()).processEvent(event);

    // store new PGExternalWebhookRequest for the webhook event
    PGExternalRequest pgExtRequest =
        pgExternalRequestService.storeNewPGExternalRequestForWebhook(event, isProcessSuccess);

    // update the PGExternalRequest status based on the event type
    pgExternalRequestService.updateStatus(pgExtRequest, PGRequestStatus.COMPLETED);
  }
}
