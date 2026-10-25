package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPGServicePort;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExtPGServiceProvider extends Provider<PGProviderType, ExternalPGServicePort> {
  protected ExtPGServiceProvider(List<ExternalPGServicePort> services) {
    super(services);
  }
}
