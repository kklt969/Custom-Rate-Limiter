package com.example.RateLimiter.LRU;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/{key}")
    public CacheResponse getData(@PathVariable String key) {
        return cacheService.getData(key);
    }

    @GetMapping("/no-cache/{key}")
    public String getfakeDBData(@PathVariable String key) {

        return cacheService.fakeDataBaseCall(key);

    }
}