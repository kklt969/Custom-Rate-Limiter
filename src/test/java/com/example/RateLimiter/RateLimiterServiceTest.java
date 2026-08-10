package com.example.RateLimiter;

import org.apache.catalina.util.RateLimiter;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RateLimiterServiceTest {

    @Test
    public void firstRequestIsAllowed() {

        RateLimiterService rateLimiterService = new RateLimiterService();

        assertTrue(rateLimiterService.allowRequest("user1"));

    }

    @Test
    public void clientIsRejectedAfterCapacityIsConsumed() {

        RateLimiterService service = new RateLimiterService();

        for (int i = 0; i < 10; i++) {
            assertTrue(service.allowRequest("user1"));
        }

        assertFalse(service.allowRequest("user1"));
    }

    @Test
    public void differentClientsHaveDifferentBuckets() {

        RateLimiterService service = new RateLimiterService();

        for (int i = 0; i < 10; i++) {
            assertTrue(service.allowRequest("user1"));
        }

        assertFalse(service.allowRequest("user1"));

        // user2 should have a completely fresh bucket
        assertTrue(service.allowRequest("user2"));
    }

    @Test
    public void sameClientSharesSameBucket() {

        RateLimiterService service = new RateLimiterService();

        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));
        assertTrue(service.allowRequest("user1"));

        assertFalse(service.allowRequest("user1"));
    }

    @Test
    public void clientCanRequestAgainAfterRefill() throws InterruptedException {

        RateLimiterService service = new RateLimiterService();

        for (int i = 0; i < 10; i++) {
            assertTrue(service.allowRequest("user1"));
        }

        assertFalse(service.allowRequest("user1"));

        Thread.sleep(1000);

        assertTrue(service.allowRequest("user1"));
    }

    @Test
    public void concurrentRequestsFromDifferentClients() throws Exception {

        RateLimiterService service = new RateLimiterService();

        AtomicInteger user1Success = new AtomicInteger();
        AtomicInteger user2Success = new AtomicInteger();

        Thread[] threads = new Thread[20];

        for (int i = 0; i < 10; i++) {

            threads[i] = new Thread(() -> {
                if (service.allowRequest("user1")) {
                    user1Success.incrementAndGet();
                }
            });
        }

        for (int i = 10; i < 20; i++) {

            threads[i] = new Thread(() -> {
                if (service.allowRequest("user2")) {
                    user2Success.incrementAndGet();
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(10, user1Success.get());
        assertEquals(10, user2Success.get());
    }

    @Test
    public void sameClientCannotExceedLimitUnderConcurrency() throws Exception {

        RateLimiterService service = new RateLimiterService();

        AtomicInteger successCount = new AtomicInteger();

        Thread[] threads = new Thread[100];

        for (int i = 0; i < threads.length; i++) {

            threads[i] = new Thread(() -> {

                if (service.allowRequest("user1")) {
                    successCount.incrementAndGet();
                }

            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(10, successCount.get());
    }

    @Test
    public void multipleUsers() throws Exception {


        RateLimiterService service = new RateLimiterService();

        for (int i = 0; i < 10; i++) {
            assertTrue(service.allowRequest("user1"));
        }
        assertFalse(service.allowRequest("user1"));

        for (int i = 0; i < 10; i++) {
            assertTrue(service.allowRequest("user2"));
        }
        assertFalse(service.allowRequest("user2"));


        assertTrue(service.allowRequest("user3"));
    }


}
