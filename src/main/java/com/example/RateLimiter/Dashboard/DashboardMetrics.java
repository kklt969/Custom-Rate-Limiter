package com.example.RateLimiter.Dashboard;

public class DashboardMetrics {

    private long totalRequests;

    private long allowedRequests;

    private long rejectedRequests;

    private long cacheHits;

    private long cacheMisses;

    public long getTotalRequests() {
        return totalRequests;
    }

    public long getAllowedRequests() {
        return allowedRequests;
    }

    public long getRejectedRequests() {
        return rejectedRequests;
    }

    public long getCacheHits() {
        return cacheHits;
    }

    public long getCacheMisses() {
        return cacheMisses;
    }

    public DashboardMetrics(long totalRequests, long allowedRequests, long rejectedRequests , long cacheHits, long cacheMisses) {
        this.totalRequests = totalRequests;
        this.allowedRequests = allowedRequests;
        this.cacheHits = cacheHits;
        this.cacheMisses = cacheMisses;
        this.rejectedRequests = rejectedRequests;

    }

}
