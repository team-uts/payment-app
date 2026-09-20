package dev.teamuts.payment.domain.pg.constant;

public enum PGProviderType {
  STRIPE("stripe"),
  ;

  private final String pgName;

  PGProviderType(String pgName) {
    this.pgName = pgName;
  }
}
