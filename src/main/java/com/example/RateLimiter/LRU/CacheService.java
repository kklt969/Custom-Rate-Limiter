package com.example.RateLimiter.LRU;


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

 */

@Service
public class CacheService {


    private final LRUCache cache;

    private final AtomicInteger databaseCalls= new AtomicInteger(0);

    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public CacheService() {

        cache = new LRUCache(100);
    }


    public CacheResponse getData(String key)  {

        String cachedData = cache.get(key);


        if(cachedData != null) {
            return new CacheResponse(cachedData,true);
        }

        ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());

       // only one thread at a time can execute this block.
        lock.lock();


        try {
            cachedData = cache.get(key);

            if(cachedData == null) {

                String dbResult = fakeDataBaseCall(key);
                cache.put(key,dbResult,30);
                return new CacheResponse(dbResult,false);

            }
            return new CacheResponse(cachedData, true);
        }finally {
            lock.unlock();
        }


    }

    public String fakeDataBaseCall(String key)  {

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
