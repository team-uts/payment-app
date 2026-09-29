package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGRequestStatus;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.infra.provider.ExternalPGServiceProvider;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestReaderPort;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestStorePort;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PGExternalRequestService {
  private final PGExternalRequestReaderPort pgExternalRequestReaderPort;
  private final PGExternalRequestStorePort pgExternalRequestStorePort;
  private final ExternalPGServiceProvider pgServiceProvider;

  @AppTransactional
  public PGExternalRequest initializePaymentMethodRequest(PGAccount pgAccount) {
    ExtPGPaymentMethodOperationDto response =
        pgServiceProvider
            .getInstance(pgAccount.getPgProvider())
            .setupPaymentMethodRequest(pgAccount);

    PGExternalRequest newExternalRequest = PGExternalRequest.initPaymentMethodSetup(response);

    return pgExternalRequestStorePort.storeNew(newExternalRequest);
  }

  public List<PGExternalRequest> getPGExternalRequestList(
      PGProviderType pgProvider, Long memberId, String pgRequestId) {
    return pgExternalRequestReaderPort.retrievePGExternalRequestList(
        pgProvider, memberId, pgRequestId);
  }

  @AppTransactional
  public PGExternalRequest storeNewPGExternalRequestForWebhook(
      ExtPGWebhookEventDto webhookEvent, boolean processResult) {
    PGExternalRequest newPgExternalRequest =
        PGExternalRequest.newWebhookEvent(webhookEvent, processResult);

    return pgExternalRequestStorePort.storeNew(newPgExternalRequest);
  }

  @AppTransactional
  public void updateStatus(PGExternalRequest pgExternalRequest, PGRequestStatus status) {}
}
