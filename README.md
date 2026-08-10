# Custom Rate Limiter & Cache

A Spring Boot project I built to learn how caching, concurrency, and rate limiting work under the hood.

Instead of starting with Redis or a caching library, I first built the cache myself and tested it with concurrent requests.

## Features

- LRU cache using a HashMap and doubly linked list
- O(1) cache get and put
- TTL-based expiration
- Cache-aside pattern
- Cache stampede protection
- Per-key locking with ReentrantLock
- Thread-safe LRU cache
- Token Bucket rate limiter
- Concurrent unit tests
- HTTP latency benchmarking
- p50 and p99 latency measurements

## Concurrency

One of the main things I wanted to understand was what happens when multiple requests ask for the same data at the same time.

I added per-key locking so that if 20 requests miss the same cache entry, only one request goes to the database while the others wait for the cache to be populated.

Example:

20 requests
     ↓
same cache key
     ↓
1 database call
     ↓
cache populated
     ↓
19 cache hits

I also made the LRU data structure thread-safe because get() and put() modify the underlying HashMap and doubly linked list.

## Benchmarking

I added a small Java HTTP benchmark to measure request latency and cache performance.

Example result:

Requests: 100
Cache hits: 99
Cache misses: 1

p50: ~4 ms
p99: ~419 ms

The benchmarking helped me see the difference between normal cache latency and slower requests under concurrent load.

## Tech Stack

- Java
- Spring Boot
- Maven
- JUnit
- Java HTTP Client
- ConcurrentHashMap
- ReentrantLock
- CountDownLatch
