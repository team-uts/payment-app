package dev.teamuts.payment.persistence.cache.lock;

import dev.teamuts.shared.data.lock.CacheLockPort;
import dev.teamuts.shared.domain.annotation.PersistenceAdapter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
  private static final Map<String, RLock> lockMap = new ConcurrentHashMap<>();

  @Override
  public boolean lock(String key, long ttlInMillis) {

    RLock rLock = redissonClient.getLock(key);

    String storageKey = getStorageKey(key, Thread.currentThread().threadId());

    try {
      boolean result = rLock.tryLock(ttlInMillis, ttlInMillis, TimeUnit.MILLISECONDS);
      lockMap.put(storageKey, rLock);
      return result;
    } catch (Exception e) {
      log.error("Failed to acquire lock for key: {}", key, e);
      return false;
    }
  }

  @Override
  public void unlock(String key) {
    String storageKey = getStorageKey(key, Thread.currentThread().threadId());
    RLock rLock = lockMap.get(storageKey);

    if (rLock != null) {
      lockMap.remove(storageKey);
      rLock.unlock();
    }
  }

  // only in synchronized block, threadId is unique
  private String getStorageKey(String key, Long threadId) {
    return "%d-%s".formatted(threadId, key);
  }
}
