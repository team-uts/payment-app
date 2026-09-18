package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.port.infra.ExtPaymentGatewayApiPort;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestStorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PGExternalRequestService {
  private final PGExternalRequestStorePort pgExternalRequestStorePort;
  private final ExtPaymentGatewayApiPort extPaymentGatewayApiPort;
}
