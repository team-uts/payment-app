package dev.teamuts.payment.persistence.cache.lock;

import dev.teamuts.shared.data.lock.CacheLockPort;
import dev.teamuts.shared.data.lock.LockReleaseCallback;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@Slf4j
@PersistenceAdapter("redissonCacheLockAdapter")
@RequiredArgsConstructor
public class RedissonCacheLockAdapter implements CacheLockPort {
  private final RedissonClient redissonClient;

  @Override
  public Optional<LockReleaseCallback> lock(String key, long ttlInMillis) {
    RLock rLock = redissonClient.getLock(key);

    try {
      boolean isAcquired = rLock.tryLock(ttlInMillis, ttlInMillis, TimeUnit.MILLISECONDS);

      if (!isAcquired) {
        return Optional.empty();
      }

      return Optional.of(() -> unlock(rLock));
    } catch (Exception e) {
      log.error("Failed to acquire lock for key: {}", key, e);
      return Optional.empty();
    }
  }

  private void unlock(RLock rLock) {
    try {
      if (rLock.isHeldByCurrentThread()) {
        rLock.unlock();
      }
    } catch (IllegalMonitorStateException e) {
      log.warn("Attempted to unlock a lock not held by the current thread: {}", rLock.getName(), e);
    } catch (Exception e) {
      log.error("Failed to unlock the lock: {}", rLock.getName(), e);
    }
  }
}
