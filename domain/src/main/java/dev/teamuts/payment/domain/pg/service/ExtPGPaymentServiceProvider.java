package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPGPaymentServicePort;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExtPGPaymentServiceProvider
    extends Provider<PGProviderType, ExternalPGPaymentServicePort> {
  protected ExtPGPaymentServiceProvider(List<ExternalPGPaymentServicePort> services) {
    super(services);
  }
}
