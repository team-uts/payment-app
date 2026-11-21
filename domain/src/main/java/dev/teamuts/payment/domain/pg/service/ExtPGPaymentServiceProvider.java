package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPgPaymentServicePort;
import dev.teamuts.shared.utils.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExtPGPaymentServiceProvider
    extends Provider<PgProviderType, ExternalPgPaymentServicePort> {
  protected ExtPGPaymentServiceProvider(List<ExternalPgPaymentServicePort> services) {
    super(services);
  }
}
