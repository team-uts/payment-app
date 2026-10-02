package dev.teamuts.payment.domain.pg.facade;

import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.service.PGExternalRequestService;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PGExternalRequestFacade {
  private final PGExternalRequestService pgExternalRequestService;

  @AppTransactional
  public PGExternalRequest getInitWebhookRequestOrRegisterInitWebhookRequest(
      ExtPGWebhookEventDto event) {
    return pgExternalRequestService
        .getPGExternalRequestNullable(
            event.getPgProvider(),
            event.getMemberId(),
            event.getPgRequestId(),
            event.getPgOperationName(),
            event.getPgDetailedMessage())
        .orElseGet(() -> pgExternalRequestService.registerInitWebhookRequest(event));
  }
}
