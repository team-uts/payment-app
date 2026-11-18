package dev.teamuts.payment.persistence.cache.lock;

import dev.teamuts.payment.persistence.cache.RedisService;
import dev.teamuts.shared.data.lock.CacheLockPort;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import java.time.Duration;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class LettuceCacheLockAdapter implements CacheLockPort {
  private final RedisService redisService;

  @Override
  public boolean lock(String key, long ttlInMillis) {
    return redisService.setIfAbsent(key, "locked", Duration.ofMillis(ttlInMillis));
  }

  @Override
  public void unlock(String key) {
    redisService.delete(key);
  }
}
