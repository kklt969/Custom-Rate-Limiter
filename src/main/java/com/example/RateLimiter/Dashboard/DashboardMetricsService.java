package com.example.RateLimiter.Dashboard;

import com.example.RateLimiter.RateLimitInterceptor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class DashboardMetricsService {

    private final AtomicLong totalRequests = new AtomicLong();
    private final AtomicLong allowedRequests = new AtomicLong();
    private final AtomicLong rejectedRequests = new AtomicLong();
    private final AtomicLong cacheHits = new AtomicLong();
    private final AtomicLong cacheMisses = new AtomicLong();



    public void recordRequest() {

        totalRequests.incrementAndGet();

    }

    public void recordAllowedRequest() {
        allowedRequests.incrementAndGet();
    }

    public void recordRejectedRequest() {
        rejectedRequests.incrementAndGet();
    }

    public void recordCacheHit() {
        cacheHits.incrementAndGet();
    }
    public void recordCacheMiss() {
        cacheMisses.incrementAndGet();
    }

    public long getTotalRequests() {
        return totalRequests.get();
    }

    public long getAllowedRequests() {
        return allowedRequests.get();
    }
    public long getRejectedRequests() {
        return rejectedRequests.get();
    }

    public long getCacheHits() {
        return cacheHits.get();
    }
    public long getCacheMisses() {
        return cacheMisses.get();

    }

}
