package dev.teamuts.payment.persistence.cache;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisService {
  private final RedisTemplate<String, Object> redisTemplate;

  public Boolean setIfAbsent(String key, String value, Duration duration) {
    return redisTemplate.opsForValue().setIfAbsent(key, value, duration);
  }
}
