package dev.teamuts.payment.domain.common.exception;

import dev.teamuts.payment.domain.pg.model.ExtPgPaymentTransaction;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExtPGApiException extends RuntimeException {
  private final Long transactionId;
  private final String pgResponseCode;
  private final String pgErrorCode;
  private final String pgErrorMessage;
  private final String pgOperationName;

  @Builder(access = AccessLevel.PRIVATE)
  private ExtPGApiException(
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

  public static ExtPGApiException transactionFailed(ExtPgPaymentTransaction pgTransaction) {
    return ExtPGApiException.builder()
        .errorMessage("External Payment Gateway Transaction API call failed")
        .transactionId(pgTransaction.getTransactionId())
        .pgResponseCode(pgTransaction.getPgResponseCode())
        .pgErrorCode(pgTransaction.getPgErrCode())
        .pgErrorMessage(pgTransaction.getPgErrMessage())
        .pgOperationName(pgTransaction.getPgOperationName())
        .build();
  }
}
