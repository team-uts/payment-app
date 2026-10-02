package dev.teamuts.payment.persistence.pg.adapter;

import dev.teamuts.payment.domain.pg.constant.PGProviderType;
import dev.teamuts.payment.domain.pg.model.PGExternalRequest;
import dev.teamuts.payment.domain.pg.port.persistence.PGExternalRequestReaderPort;
import dev.teamuts.payment.persistence.common.annotation.PersistenceAdapter;
import dev.teamuts.payment.persistence.pg.mapper.PGExternalRequestConverter;
import dev.teamuts.payment.persistence.pg.repository.PGExternalRequestRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PGExternalRequestReaderAdapter implements PGExternalRequestReaderPort {
  private final PGExternalRequestRepository pgExternalRequestRepository;
  private final PGExternalRequestConverter pgExternalRequestConverter;

  /**
   * Be careful not to lock by these parameters, because there may be multiple records with the same
   * parameters. This could cause a deadlock because the pgRequestId is not unique, and the same
   * pgRequestId can be used for multiple requests, which can lead to gap lock or unintended lock.
   */
  @Override
  public PGExternalRequest retrieveSingleByParameters(
      String pgRequestId, PGProviderType pgProvider, Long memberId, String extOperation) {
    return pgExternalRequestRepository
        .findTopByPgRequestIdAndMemberIdAndPgProviderAndExtOperationOrderByIdDesc(
            pgRequestId, memberId, pgProvider, extOperation)
        .map(pgExternalRequestConverter::covertToDomainModel)
        .orElseThrow(
            () ->
                new RuntimeException(
                    "PGExternalRequest not found with parameters: pgProvider=%s, memberId=%d, pgRequestId=%s, extOperation=%s"
                        .formatted(pgProvider.name(), memberId, pgRequestId, extOperation)));
  }

  @Override
  public Optional<PGExternalRequest> retrieveSingleByParametersNullable(
      String pgRequestId,
      PGProviderType pgProvider,
      Long memberId,
      String extOperation,
      String extDetailedMessage) {
    return pgExternalRequestRepository
        .findTopByPgRequestIdAndMemberIdAndPgProviderAndExtOperationAndExtDetailedMessageOrderByIdDesc(
            pgRequestId, memberId, pgProvider, extOperation, extDetailedMessage)
        .map(pgExternalRequestConverter::covertToDomainModel);
  }
}
