package dev.teamuts.payment.domain.event.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum OutboxEventStatus {
  INIT,
  PROCESSED,
  FAILED,
}
