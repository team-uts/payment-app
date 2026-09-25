package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.dto.ExtPGPaymentMethodOperationDto;
import dev.teamuts.payment.domain.pg.model.PGAccount;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.infra.provider.ExternalPGServiceProvider;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestStorePort;
import dev.teamuts.payment.shared.data.AppTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PGExternalRequestService {
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
}
