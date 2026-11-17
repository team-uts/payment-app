package dev.teamuts.payment.persistence.cache.lock;

import dev.teamuts.payment.domain.common.cache.CacheLockPort;
import dev.teamuts.payment.persistence.cache.RedisService;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class LettuceCacheLockAdapter implements CacheLockPort {
  private final RedisService redisService;

  @Override
  public boolean lock(String key, long ttl) {
    return false;
  }

  @Override
  public void unlock(String key) {}
}
