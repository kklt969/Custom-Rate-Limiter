package com.example.RateLimiter.LRU;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheServiceTest {

    /*
    20 threads
   ↓
CountDownLatch
   ↓
all released together
   ↓
same key: user1
   ↓
per-key synchronized lock
   ↓
1 DB call
   ↓
19 cache hits
     */

    @Test
    public void twentyThreadCalls() throws InterruptedException {

        CacheService cacheService = new CacheService();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread[] threads = new Thread[50]; // create thread

        for (int i = 0; i < threads.length; i++) { // give thread what work to do.
            threads[i] = new Thread(() -> {

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                cacheService.getData("user1");


            });
        }

        for (Thread thread : threads) {
            thread.start(); // start thread works

        }

        countDownLatch.countDown();

        for (Thread thread : threads) {
            thread.join(); // wait until all finish

        }

        assertEquals(1, cacheService.getDatabaseCalls());

    }

    @Test
    public void concurrentLRUTest() throws InterruptedException {

        LRUCache cache = new LRUCache(10);

        CountDownLatch startLatch = new CountDownLatch(1);

        Thread[] threads = new Thread[50];

        // Create 50 threads
        for (int i = 0; i < threads.length; i++) {

            final int threadNumber = i;

            threads[i] = new Thread(() -> {

                try {
                    // Wait until all threads are ready
                    startLatch.await();

                    // Each thread puts its own key
                    cache.put(
                            "user" + threadNumber,
                            "data" + threadNumber,
                            30
                    );

                    // Then get it
                    cache.get("user" + threadNumber);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }

        // GO!
        startLatch.countDown();

        // Wait for everyone to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // Cache should never exceed capacity
        assertTrue(cache.size() <= 10);
    }


}
