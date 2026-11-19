package dev.teamuts.payment.persistence.cache;

import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LockRedisService {
  private final StringRedisTemplate lockRedisTemplate;

  private static final DefaultRedisScript<Long> DELETE_KEY =
      new DefaultRedisScript<>(
          """
          if redis.call('GET', KEYS[1]) == ARGV[1] then
              return redis.call('DEL', KEYS[1])
          end
          return 0
          """,
          Long.class);

  public Boolean setIfAbsent(String key, String value, Duration duration) {
    return lockRedisTemplate.opsForValue().setIfAbsent(key, value, duration);
  }

  public void removeAfterCheck(String key, String value) {
    lockRedisTemplate.execute(DELETE_KEY, List.of(key), value);
  }
}
