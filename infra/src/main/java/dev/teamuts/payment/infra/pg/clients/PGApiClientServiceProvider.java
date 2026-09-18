package dev.teamuts.payment.infra.pg.clients;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.shared.provider.Provider;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PGApiClientServiceProvider extends Provider<PGProviderType, PGApiClientService> {

  protected PGApiClientServiceProvider(List<PGApiClientService> services) {
    super(services);
  }
}
