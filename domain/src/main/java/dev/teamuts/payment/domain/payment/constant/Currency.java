package dev.teamuts.payment.domain.payment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {
  AUD(2),
  USD(2),
  KRW(0);

  private final int scale;
}
