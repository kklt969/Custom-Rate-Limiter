package com.example.RateLimiter.LRU;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LRUCacheTest {
/*
    @Test
    public void getExistingKey() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple");

        assertEquals("Apple", cache.get("A"));


    }

    @Test
    public void getMissingKey() {

        LRUCache cache = new LRUCache(3);

        assertNull(cache.get("A"));
    }


    @Test
    public void storesMultipleValues() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple");
        cache.put("B", "Banana");
        cache.put("C", "Cat");

        assertEquals("Apple", cache.get("A"));
        assertEquals("Banana", cache.get("B"));
        assertEquals("Cat", cache.get("C"));
    }

    @Test
    public void evictsLeastRecentlyUsed() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple");
        cache.put("B", "Banana");
        cache.put("C", "Cat");

        cache.put("D", "Dog");

        assertNull(cache.get("A"));
    }

    @Test
    public void getMakesItemMostRecentlyUsed() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple");
        cache.put("B", "Banana");
        cache.put("C", "Cat");

        cache.get("A");

        cache.put("D", "Dog");

        assertEquals("Apple", cache.get("A"));
        assertNull(cache.get("B"));
    }

    @Test
    public void complexLruOrder() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple");
        cache.put("B", "Banana");
        cache.put("C", "Cat");

        cache.get("A");

        cache.put("D", "Dog");

        assertNull(cache.get("B"));

        cache.get("C");

        cache.put("E", "Egg");

        assertNull(cache.get("A"));
        assertEquals("Dog", cache.get("D"));
        assertEquals("Cat", cache.get("C"));
        assertEquals("Egg", cache.get("E"));
    }

    */

    @Test
    public void itemIsValidBeforeExpiration() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple", 2);

        assertEquals("Apple", cache.get("A"));
    }

    @Test
   public void itemExpiresAfterTTL() throws InterruptedException {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple", 1);

        Thread.sleep(1100);

        assertNull(cache.get("A"));
    }

    @Test
    public void itemsCanHaveDifferentTTLs() throws InterruptedException {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple", 1);
        cache.put("B", "Banana", 3);

        Thread.sleep(1100);

        assertNull(cache.get("A"));
        assertEquals("Banana", cache.get("B"));
    }

    @Test
   public void updatingKeyResetsTTL() throws InterruptedException {

        LRUCache cache = new LRUCache(3);

        cache.put("A", "Apple", 1);

        Thread.sleep(700);

        cache.put("A", "Avocado", 2);

        Thread.sleep(700);

        assertEquals("Avocado", cache.get("A"));
    }

    @Test
    public void negativeTTL() throws InterruptedException {
        LRUCache cache = new LRUCache(3);
        cache.put("A", "Apple", 0);

        assertNull(cache.get("A"));
    }





}
