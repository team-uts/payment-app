package dev.teamuts.payment.domain.event.constant;

public enum OutboxEventStatus {
  INIT,
  PENDING,
  PROCESSED,
  FAILED
}
