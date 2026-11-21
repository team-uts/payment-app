package dev.teamuts.payment.domain.pg.service;

import dev.teamuts.payment.domain.pg.constant.PgProviderType;
import dev.teamuts.payment.domain.pg.port.infra.ExternalPgServicePort;
import dev.teamuts.shared.utils.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExtPGServiceProvider extends Provider<PgProviderType, ExternalPgServicePort> {
  protected ExtPGServiceProvider(List<ExternalPgServicePort> services) {
    super(services);
  }
}
