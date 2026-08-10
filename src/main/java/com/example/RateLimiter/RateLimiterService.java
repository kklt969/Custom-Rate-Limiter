package com.example.RateLimiter;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/*
buckets
 ├── user1 → TokenBucket
 ├── user2 → TokenBucket
 └── user3 → TokenBucket

 */

@Service
public class RateLimiterService {

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();



    private int capacity = 101;

    private int refillRatePerSecond = 1;


    public boolean allowRequest(String clientId) {

        TokenBucket bucket = buckets.computeIfAbsent(clientId,
                key -> new TokenBucket(capacity, refillRatePerSecond));

      return bucket.tryConsume();

    }

}
