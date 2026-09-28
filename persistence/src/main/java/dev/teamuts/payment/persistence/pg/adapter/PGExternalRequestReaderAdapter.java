package dev.teamuts.payment.persistence.pg.adapter;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestReaderPort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.pg.mapper.PGExternalRequestConverter;
import dev.teamuts.payment.persistence.pg.repository.PGExternalRequestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PGExternalRequestReaderAdapter implements PGExternalRequestReaderPort {
  private final PGExternalRequestRepository pgExternalRequestRepository;
  private final PGExternalRequestConverter pgExternalRequestConverter;

  @Override
  public List<PGExternalRequest> retrievePGExternalRequestList(
      PGProviderType pgProvider, Long memberId, String pgRequestId) {
    return pgExternalRequestRepository
        .findAllByPgRequestIdAndMemberIdAndPgProvider(pgRequestId, memberId, pgProvider)
        .stream()
        .map(pgExternalRequestConverter::covertToDomainModel)
        .toList();
  }
}
