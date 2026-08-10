package com.example.RateLimiter;

import static java.lang.Math.min;

import java.time.Instant;

public class TokenBucket {

    private int capacity;

    private int tokenCount;

    private int refillRatePerSecond;

    private long lastRefillTime;

    public TokenBucket(int capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokenCount = capacity;

    }

    public synchronized boolean tryConsume() {

        refill();

        if (tokenCount > 0) {
            tokenCount--;
            return true;
        }
        return false;


    }

    private void refill() {

        long currentTime = Instant.now().getEpochSecond();

        long elapsedTime = (currentTime - lastRefillTime);

        int tokensToAdd = (int) (elapsedTime * refillRatePerSecond);

        if (tokensToAdd > 0) {

            tokenCount = Math.min(capacity, tokenCount + tokensToAdd);

            lastRefillTime = currentTime;

        }

    }

    public int getTokenCount() {
        return tokenCount;
    }
}

