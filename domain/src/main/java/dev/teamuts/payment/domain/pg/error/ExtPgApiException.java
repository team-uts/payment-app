package dev.teamuts.payment.domain.pg.error;

import dev.teamuts.payment.domain.pg.model.sub.ExtPgPaymentTransaction;
import dev.teamuts.shared.error.exception.ApiInternalException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExtPgApiException extends ApiInternalException {
  private final Long transactionId;
  private final String pgResponseCode;
  private final String pgErrorCode;
  private final String pgErrorMessage;
  private final String pgOperationName;

  @Builder(access = AccessLevel.PRIVATE)
  private ExtPgApiException(
      String errorMessage,
      Long transactionId,
      String pgResponseCode,
      String pgErrorCode,
      String pgErrorMessage,
      String pgOperationName) {
    super(errorMessage);
    this.transactionId = transactionId;
    this.pgResponseCode = pgResponseCode;
    this.pgErrorCode = pgErrorCode;
    this.pgErrorMessage = pgErrorMessage;
    this.pgOperationName = pgOperationName;
  }

  public static ExtPgApiException transactionFailed(ExtPgPaymentTransaction pgTransaction) {
    return ExtPgApiException.builder()
        .errorMessage("External Payment Gateway Transaction API call failed")
        .transactionId(pgTransaction.getTransactionId())
        .pgResponseCode(pgTransaction.getPgResponseCode())
        .pgErrorCode(pgTransaction.getPgErrCode())
        .pgErrorMessage(pgTransaction.getPgErrMessage())
        .pgOperationName(pgTransaction.getPgOperationName())
        .build();
  }
}
