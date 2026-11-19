package dev.teamuts.payment.persistence.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;

@ConfigurationProperties(prefix = "spring.data.redis.redisson")
public record RedissonPoolProperties(DataRedisProperties.Pool pool) {}
