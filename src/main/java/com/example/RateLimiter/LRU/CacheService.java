package com.example.RateLimiter.LRU;


import com.example.RateLimiter.Redis.RedisCacheService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/*
cache.get()
    ↓
HIT → return immediately

MISS
 ↓
get ReentrantLock for key
 ↓
lock.lock()
 ↓
SECOND cache.get()
 ↓
HIT → return
 ↓
MISS
 ↓
database
 ↓
cache.put()
 ↓
finally
 ↓
unlock()

----
                CacheService
                     │
             ┌───────┴───────┐
             │               │
         Redis GET       ReentrantLock
             │               │
             ↓               ↓
         Cache HIT       Cache MISS
                             │
                             ↓
                        Database
                             │
                             ↓
                       Redis SET
                         + TTL

 */

@Service
public class CacheService {


    //private final LRUCache cache;

    private final AtomicInteger databaseCalls= new AtomicInteger(0);

    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    private final RedisCacheService redisCacheService;

    public CacheService(RedisCacheService redisCacheService) {
        //this.cache = cache;
        this.redisCacheService = redisCacheService;
    }

    //public CacheService() {}


    public CacheResponse getData(String key)  {

        String cachedData = redisCacheService.get(key);


        if(cachedData != null) {
            return new CacheResponse(cachedData,true);
        }

        ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());

       // only one thread at a time can execute this block.
        lock.lock();


        try {
            cachedData = redisCacheService.get(key);

            if(cachedData == null) {

                String dbResult = fakeDataBaseCall(key);
                redisCacheService.set(key,dbResult,30);
                return new CacheResponse(dbResult,false);

            }
            return new CacheResponse(cachedData, true);
        }finally {
            lock.unlock();
        }


    }

    private String fakeDataBaseCall(String key)  {

        try{
            Thread.sleep(200);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        databaseCalls.incrementAndGet();
        return "Databaseresult for " + key;

    }

    public int getDatabaseCalls() {
        return databaseCalls.get();
    }



}
