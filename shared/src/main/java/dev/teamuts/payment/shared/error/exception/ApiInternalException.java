package dev.teamuts.payment.shared.error.exception;

/** Only for app internal use. */
public class ApiInternalException extends RuntimeException {
  protected ApiInternalException(String message) {
    super(message);
  }

  protected ApiInternalException(String message, Throwable cause) {
    super(message, cause);
  }
}
