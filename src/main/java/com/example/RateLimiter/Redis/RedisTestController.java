package com.example.RateLimiter.Redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisTestController {

    private final StringRedisTemplate redis;

    private final RedisCacheService redisCacheService;

    public RedisTestController(StringRedisTemplate redis, RedisCacheService redisCacheService) {
        this.redis = redis;
        this.redisCacheService = redisCacheService;
    }

    @GetMapping("/redis-test")
    public String testRedis() {

        redisCacheService.set("user2", "testing", 55);
        return redisCacheService.get("user2");

    }


}