package com.example.RateLimiter.Redis;


import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisRateLimiterService {

    private final StringRedisTemplate redisTemplate;

    private final DefaultRedisScript<Long> redisScript;

    private boolean enabled = false;

    public RedisRateLimiterService(StringRedisTemplate redisTemplate) {

        this.redisTemplate = redisTemplate;

        this.redisScript = new DefaultRedisScript<>();
        this.redisScript.setLocation(
                new ClassPathResource("Redis/token_bucket.lua")
        );
        this.redisScript.setResultType(Long.class);
    }

    public boolean allow(String key){

        if(!enabled){
            return true;
        }

        String redisKey = "rate_limit:" + key;

        long capacity = 5;

        double refillRate = 0.001; // 1 PER SECOND

        long currentTime = System.currentTimeMillis();


        Long result = redisTemplate.execute(
                redisScript,
                List.of(redisKey),
                String.valueOf(capacity),
                String.valueOf(refillRate),
                String.valueOf(currentTime)
        );

        return result != null && result == 1;


    }


}
