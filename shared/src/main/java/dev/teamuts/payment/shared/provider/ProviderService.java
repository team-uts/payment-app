package dev.teamuts.payment.shared.provider;

public interface ProviderService<T extends Enum<T>> {
  boolean supports(T key);
}
