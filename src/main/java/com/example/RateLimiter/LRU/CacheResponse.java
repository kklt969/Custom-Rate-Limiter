package com.example.RateLimiter.LRU;

public class CacheResponse {

    private String data;
    private boolean cacheHit;

    public CacheResponse(String data, boolean cacheHit) {
        this.data = data;
        this.cacheHit = cacheHit;
    }

    public String getData() {
        return data;
    }

    public boolean isCacheHit() {
        return cacheHit;
    }



}
