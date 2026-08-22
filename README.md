# Custom Rate Limiter

A Spring Boot project where I built a cache and rate limiter from scratch, then moved the system to Redis to handle concurrency and distributed state.

## What I Built

- Custom LRU cache using HashMap + Doubly Linked List
- TTL-based cache expiration
- Thread-safe cache operations
- Per-key ReentrantLock for cache stampede protection
- Redis Cloud integration
- Redis-backed caching
- Redis Lua script for atomic token bucket rate limiting
- HTTP 429 rate-limit responses
- Concurrent tests with 50 threads
- Basic latency benchmarking

## Architecture

Client
  ↓
Spring Boot
  ↓
RateLimitInterceptor
  ↓
Redis Token Bucket
  ↓
Lua Script
  ↓
Redis Cloud

The cache uses Redis for shared state, while the token bucket is executed atomically inside Redis using Lua.

## Concurrency

The cache was tested with 50 concurrent requests for the same missing key.

Result:

- 50 requests
- 1 database call
- 49 cache hits

The Redis rate limiter was also tested with 50 concurrent requests against the same key.

Result:

- 5 requests allowed
- 45 requests rejected

## Benchmark

### Redis Cloud — Mostly Cache Hits

100 requests using 5 keys:

- Cache hit rate: 96%
- p50: 75.81 ms
- p99: 1581.20 ms

### Redis Cloud — Mostly Cache Misses

100 requests using 100 different keys:

- Cache hit rate: 1%
- p50: 420.77 ms
- p99: 1355.56 ms

The benchmarks measure end-to-end HTTP latency, including Spring Boot and network communication with Redis Cloud.

## What I Learned

- How LRU caching works internally
- Race conditions and thread safety
- synchronized vs ReentrantLock
- Cache stampede prevention
- Redis and TTL
- Atomic Redis operations
- Lua scripting
- Distributed rate limiting
- p50 vs p99 latency
- Concurrent testing

## Tech Stack

- Java
- Spring Boot
- Spring Data Redis
- Redis Cloud
- Lua
- JUnit
- Maven
