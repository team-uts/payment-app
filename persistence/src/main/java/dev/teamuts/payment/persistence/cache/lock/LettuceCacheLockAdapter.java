package dev.teamuts.payment.persistence.cache.lock;

import dev.teamuts.payment.persistence.cache.LockRedisService;
import dev.teamuts.shared.data.lock.CacheLockPort;
import dev.teamuts.shared.data.lock.LockReleaseCallback;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter("lettuceCacheLockAdapter")
@RequiredArgsConstructor
public class LettuceCacheLockAdapter implements CacheLockPort {
  private final LockRedisService redisService;

  @Override
  public Optional<LockReleaseCallback> lock(String key, long ttlInMillis) {
    String token = UUID.randomUUID().toString();
    Boolean lockAcquired = redisService.setIfAbsent(key, token, Duration.ofMillis(ttlInMillis));

    if (lockAcquired.equals(Boolean.FALSE)) {
      return Optional.empty();
    }

    return Optional.of(() -> redisService.removeAfterCheck(key, token));
  }
}
