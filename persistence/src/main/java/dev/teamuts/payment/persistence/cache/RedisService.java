package dev.teamuts.payment.persistence.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisService {
  private final RedisTemplate<String, Object> redisTemplate;
}
