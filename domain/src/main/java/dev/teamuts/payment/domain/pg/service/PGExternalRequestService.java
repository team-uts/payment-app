package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.constant.PGWebhookEventProcessResultType;
import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.dto.ExtPGWebhookEventDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.infra.provider.ExternalPGServiceProvider;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestReaderPort;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestStorePort;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestUpdatePort;
import dev.teamuts.payment.shared.data.AppTransactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@AppTransactional
public class PGExternalRequestService {
  private final PGExternalRequestReaderPort pgExternalRequestReaderPort;
  private final PGExternalRequestStorePort pgExternalRequestStorePort;
  private final PGExternalRequestUpdatePort pgExternalRequestUpdatePort;
  private final ExternalPGServiceProvider pgServiceProvider;

  public PGExternalRequest storeInitPaymentMethodRequest(PGAccount pgAccount) {
    ExtPGPaymentMethodOperationDto response =
        pgServiceProvider
            .getInstance(pgAccount.getPgProvider())
            .setupPaymentMethodRequest(pgAccount);

    PGExternalRequest newExternalRequest = PGExternalRequest.initPaymentMethodSetup(response);

    return pgExternalRequestStorePort.storeNew(newExternalRequest);
  }

  public PGExternalRequest getPGExternalRequest(
      PGProviderType pgProvider, Long memberId, String pgRequestId, String extOperation) {
    return pgExternalRequestReaderPort.retrieveSingleByParameters(
        pgRequestId, pgProvider, memberId, extOperation);
  }

  public Optional<PGExternalRequest> getPGExternalRequestNullable(
      PGProviderType pgProvider,
      Long memberId,
      String pgRequestId,
      String extOperation,
      String extDetailedMessage) {
    return pgExternalRequestReaderPort.retrieveSingleByParametersNullable(
        pgRequestId, pgProvider, memberId, extOperation, extDetailedMessage);
  }

  /**
   * Registers new PGExternalRequest for a webhook event.
   *
   * @param webhookEvent The webhook event data.
   * @return The newly created PGExternalRequest.
   */
  public PGExternalRequest registerInitWebhookRequest(ExtPGWebhookEventDto webhookEvent) {
    PGExternalRequest newPgExternalRequest = PGExternalRequest.initWebhookEvent(webhookEvent);

    return pgExternalRequestStorePort.storeNew(newPgExternalRequest);
  }

  public void associateWithSourceAfterProcessWebhook(
      PGExternalRequest targetRequest,
      PGExternalRequest sourceRequest,
      PGWebhookEventProcessResultType processResultType) {
    targetRequest.associateWithSource(sourceRequest);
    targetRequest.updateStatusBasedOnWebhookProcessingResult(processResultType);

    pgExternalRequestUpdatePort.updateSourceRequestIdAndStatus(targetRequest);
  }

  public void completeSourceRequest(PGExternalRequest pgExternalRequest) {
    pgExternalRequest.completed();

    pgExternalRequestUpdatePort.updateStatus(pgExternalRequest);
  }
}
