package ru.naumen.calorietracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * Конфигурация кэширования с использованием Redis.
 */
@Configuration
public class RedisConfig {

    /**
     * Создает менеджер кэша Redis с TTL 15 минут.
     *
     * @param factory фабрика подключения к Redis
     * @return менеджер кэша Redis
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }
}


