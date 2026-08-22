package com.example.RateLimiter;

import com.example.RateLimiter.Redis.RedisRateLimiterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class RedisRateLimiterServiceTest {

    @Autowired
    private RedisRateLimiterService redisRateLimiterService;


    public void tokenBucketTest() {

        String key = "test-user-" + System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {

            boolean allowed = redisRateLimiterService.allow(key);
            System.out.println(
                    "Request " + i + " allowed: " + allowed);
        }

    }


    @Test
    public void fiftyConcurrentRequests() throws InterruptedException {

        int numberOfThreads = 50;

        String key = "concurrent-test-2" + System.currentTimeMillis();

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger allowed = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {

            threads[i] = new Thread(() -> {

                try {
                    startLatch.await();

                    boolean result =
                            redisRateLimiterService.allow(key);

                    if (result) {
                        allowed.incrementAndGet();
                    } else {
                        rejected.incrementAndGet();
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Release them at approximately the same time
        startLatch.countDown();

        // Wait until all 50 finish
        finishLatch.await();

        System.out.println("Allowed: " + allowed.get());
        System.out.println("Rejected: " + rejected.get());

        assertEquals(5, allowed.get());
        assertEquals(45, rejected.get());

    }





}
