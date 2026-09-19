package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.infra.ExtPaymentGatewayApiPort;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestStorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PGExternalRequestService {
  private final PGExternalRequestStorePort pgExternalRequestStorePort;
  private final ExtPaymentGatewayApiPort extPaymentGatewayApiPort;

  public PGExternalRequest initializePaymentMethodRequest(PGAccount pgAccount) {
    ExtPGPaymentMethodOperationDto response =
        extPaymentGatewayApiPort.setupPaymentMethodRequest(pgAccount);

    PGExternalRequest newExternalRequest = PGExternalRequest.initPaymentMethodSetup(response);

    return pgExternalRequestStorePort.storeNew(newExternalRequest);
  }
}
