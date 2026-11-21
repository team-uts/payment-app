package dev.teamuts.payment.domain.pg.model;

import dev.teamuts.payment.domain.payment.constant.PaymentTransactionStatus;
import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import dev.teamuts.payment.domain.pg.constant.ExtPGOperationResultStatus;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

/** External Payment Gateway Payment Transaction */
@Getter
@Builder(access = lombok.AccessLevel.PRIVATE)
public class ExtPgPaymentTransaction {
  private Long transactionId; // Internal payment transaction ID
  private String pgTransactionId;
  private String pgResponseCode; // PG transaction status from the payment gateway
  private String pgErrCode;
  private String pgErrMessage;
  private String pgOperationName;
  private ExtPGOperationResultStatus resultStatus;
  private PaymentTransactionStatus domainStatus;

  public static ExtPgPaymentTransaction fromPg(
      PaymentTransaction transaction,
      String pgTransactionId,
      String pgResponseCode,
      String errCode,
      String errMsg,
      String pgOperationName,
      PaymentTransactionStatus domainStatus) {

    checkPgTransactionIdConsistency(transaction, pgTransactionId);

    return ExtPgPaymentTransaction.builder()
        .transactionId(transaction.getId())
        .pgTransactionId(pgTransactionId)
        .pgResponseCode(pgResponseCode)
        .pgErrCode(errCode)
        .pgErrMessage(errMsg)
        .pgOperationName(pgOperationName)
        .resultStatus(ExtPGOperationResultStatus.SUCCEEDED)
        .domainStatus(domainStatus)
        .build();
  }

  public static ExtPgPaymentTransaction fromPgTest(
      PaymentTransaction transaction,
      String pgTransactionId,
      String pgResponseCode,
      String errCode,
      String errMsg,
      String pgOperationName,
      PaymentTransactionStatus domainStatus) {

    return ExtPgPaymentTransaction.builder()
        .transactionId(transaction.getId())
        .pgTransactionId(pgTransactionId)
        .pgResponseCode(pgResponseCode)
        .pgErrCode(errCode)
        .pgErrMessage(errMsg)
        .pgOperationName(pgOperationName)
        .resultStatus(ExtPGOperationResultStatus.SUCCEEDED)
        .domainStatus(domainStatus)
        .build();
  }

  public static ExtPgPaymentTransaction invalidRequest(
      PaymentTransaction transaction,
      String pgResponseCode,
      String errCode,
      String errMsg,
      String pgOperationName) {
    return ExtPgPaymentTransaction.builder()
        .transactionId(transaction.getId())
        .pgTransactionId(transaction.getPgTransactionId())
        .pgResponseCode(pgResponseCode)
        .pgErrCode(errCode)
        .pgErrMessage(errMsg)
        .pgOperationName(pgOperationName)
        .resultStatus(ExtPGOperationResultStatus.INVALID_REQUEST)
        .domainStatus(PaymentTransactionStatus.FAILED)
        .build();
  }

  public static ExtPgPaymentTransaction apiFailed(
      PaymentTransaction transaction,
      String pgResponseCode,
      String errCode,
      String errMsg,
      String pgOperationName) {
    return ExtPgPaymentTransaction.builder()
        .transactionId(transaction.getId())
        .pgTransactionId(transaction.getPgTransactionId())
        .pgResponseCode(pgResponseCode)
        .pgErrCode(errCode)
        .pgErrMessage(errMsg)
        .pgOperationName(pgOperationName)
        .resultStatus(ExtPGOperationResultStatus.FAILED)
        .domainStatus(PaymentTransactionStatus.FAILED)
        .build();
  }

  public static ExtPgPaymentTransaction timeout(
      PaymentTransaction transaction,
      String pgResponseCode,
      String errCode,
      String errMsg,
      String pgOperationName) {
    return ExtPgPaymentTransaction.builder()
        .transactionId(transaction.getId())
        .pgTransactionId(transaction.getPgTransactionId())
        .pgResponseCode(pgResponseCode)
        .pgErrCode(errCode)
        .pgErrMessage(errMsg)
        .pgOperationName(pgOperationName)
        .resultStatus(ExtPGOperationResultStatus.TIMEOUT)
        .domainStatus(PaymentTransactionStatus.PENDING)
        .build();
  }

  /**
   * if the tx has pgTransactionId, it must match the provided pgTransactionId there is a case where
   * the transaction is not associated with pgTransactionId yet.
   */
  private static void checkPgTransactionIdConsistency(
      PaymentTransaction transaction, String pgTransactionId) {
    if (transaction.getPgTransactionId() != null
        && !Objects.equals(transaction.getPgTransactionId(), pgTransactionId)) {
      throw new IllegalArgumentException(
          "PG transaction ID does not match (txId: %d, pgTxId: %s)"
              .formatted(transaction.getId(), pgTransactionId));
    }
  }

  public boolean isFailed() {
    return resultStatus == ExtPGOperationResultStatus.FAILED;
  }

  public boolean isNotInvalidRequest() {
    return resultStatus != ExtPGOperationResultStatus.INVALID_REQUEST;
  }
}
