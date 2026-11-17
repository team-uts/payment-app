package dev.teamuts.payment.domain.common.cache;

public interface CacheLockPort {
  boolean lock(String key, long ttl);

  void unlock(String key);
}
