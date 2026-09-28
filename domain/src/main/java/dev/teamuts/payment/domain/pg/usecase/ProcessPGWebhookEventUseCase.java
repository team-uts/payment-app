package dev.teamuts.payment.domain.pg.usecase;

import dev.teamuts.payment.domain.common.annotation.UseCase;
import dev.teamuts.payment.domain.pg.dto.PGExtOperationInfo.WebhookEventInfo;
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

  @AppTransactional
  public void execute(WebhookEventInfo event) {
    // retrieve PGExternalRequest List from DB
    List<PGExternalRequest> pgExtRequests =
        pgExternalRequestService.getPGExternalRequestList(
            event.getPgProvider(), event.getMemberId(), event.getPgRequestId());

    // find a single proper data according to the event type
    PGExternalRequest basePGExtRequest =
        pgServiceProvider
            .getInstance(event.getPgProvider())
            .findBaseExternalRequestForWebhook(pgExtRequests, event.getRequestType());

    // TODO process the event data based on the pg webhook type

    // store new PGExternalRequest for the webhook event
    PGExternalRequest webhookPGExtRequest =
        pgExternalRequestService.storeNewPGExternalRequestForWebhook(event);

    // TODO update the PGExternalRequest status based on the event type
  }
}
