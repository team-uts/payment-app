package dev.teamuts.payment.persistence.common.config;

import dev.teamuts.payment.persistence.common.config.properties.RedissonPoolProperties;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.api.StatefulConnection;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedissonPoolProperties.class)
@RequiredArgsConstructor
public class RedisConfig {
  private final DataRedisProperties redisProps;
  private final RedissonPoolProperties redissonPoolProps;

  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      @Qualifier("lettuceConnectionFactory") RedisConnectionFactory lettuceConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(lettuceConnectionFactory);

    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setValueSerializer(stringSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    redisTemplate.setHashValueSerializer(stringSerializer);

    // synchronize app transaction with redis transaction.
    redisTemplate.setEnableTransactionSupport(true);

    return redisTemplate;
  }

  @Primary
  @Bean("lettuceConnectionFactory")
  public RedisConnectionFactory lettuceConnectionFactory() {
    RedisStandaloneConfiguration redisConfig =
        new RedisStandaloneConfiguration(redisProps.getHost(), redisProps.getPort());

    redisConfig.setUsername(redisProps.getUsername());
    redisConfig.setPassword(redisProps.getPassword());
    redisConfig.setDatabase(redisProps.getDatabase());

    // pool configuration for lettuce
    GenericObjectPoolConfig<StatefulConnection<?, ?>> poolConfig = new GenericObjectPoolConfig<>();
    DataRedisProperties.Pool pool = redisProps.getLettuce().getPool();
    poolConfig.setMaxTotal(pool.getMaxActive());
    poolConfig.setMaxIdle(pool.getMaxIdle());
    poolConfig.setMinIdle(pool.getMinIdle());
    poolConfig.setMaxWait(pool.getMaxWait());

    LettuceClientConfiguration clientConfig =
        LettucePoolingClientConfiguration.builder()
            .clientName("payment-redis-pool")
            .commandTimeout(Objects.requireNonNull(redisProps.getTimeout()))
            .clientOptions(
                ClientOptions.builder()
                    .socketOptions(
                        SocketOptions.builder()
                            .connectTimeout(Objects.requireNonNull(redisProps.getConnectTimeout()))
                            .build())
                    .build())
            .poolConfig(poolConfig)
            .build();

    LettuceConnectionFactory connectionFactory =
        new LettuceConnectionFactory(redisConfig, clientConfig);
    // TODO: check this option for optimization in terms of redis connection management.
    connectionFactory.setShareNativeConnection(true);

    return connectionFactory;
  }

  @ConditionalOnProperty(
      name = "spring.data.redis.redisson.enabled",
      havingValue = "true",
      matchIfMissing = true)
  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.setUsername(redisProps.getUsername()).setPassword(redisProps.getPassword());

    SingleServerConfig singleConfig = config.useSingleServer();
    singleConfig
        .setAddress("redis://%s:%d".formatted(redisProps.getHost(), redisProps.getPort()))
        .setDatabase(redisProps.getDatabase())
        .setTimeout((int) Objects.requireNonNull(redisProps.getTimeout()).toMillis())
        .setConnectTimeout((int) Objects.requireNonNull(redisProps.getConnectTimeout()).toMillis())
        .setDnsMonitoringInterval(-1) // in case of Redis server failover (-1: disable monitoring)
        .setConnectionPoolSize(redissonPoolProps.pool().getMaxActive())
        .setConnectionMinimumIdleSize(redissonPoolProps.pool().getMinIdle());

    return Redisson.create(config);
  }
}
