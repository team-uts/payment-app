package dev.teamuts.payment.domain.pg.port.infra.provider;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPGServicePort;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExternalPGServiceProvider extends Provider<PGProviderType, ExternalPGServicePort> {
  protected ExternalPGServiceProvider(List<ExternalPGServicePort> services) {
    super(services);
  }
}
