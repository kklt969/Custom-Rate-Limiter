package com.example.RateLimiter.Redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {

    private final StringRedisTemplate redis;

    public RedisCacheService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void set(String key, String value, long ttlSeconds) {

        if(key != null && value != null && ttlSeconds > 0) {
            redis.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
        }
    }

    public String get(String key) {
        if(key != null) {
            return redis.opsForValue().get(key);
        }
        return null;
    }


}
