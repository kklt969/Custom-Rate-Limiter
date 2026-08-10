package com.example.RateLimiter.Benchmark;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Benchmark {

    public static void main(String[] args) throws Exception {

        AtomicInteger cacheHits = new AtomicInteger();
        AtomicInteger cacheMisses = new AtomicInteger();



        // Store latency from every request.
        // Multiple threads will access this list,
        // so we use a synchronized list.
        List<Double> latencies =
                Collections.synchronizedList(new ArrayList<>());

        // One HTTP client shared by all requests.
        HttpClient client = HttpClient.newHttpClient();

        // 10 worker threads.
        ExecutorService executor =
                Executors.newFixedThreadPool(10);

        // Store a Future for every submitted task.
        List<Future<?>> futures = new ArrayList<>();


        // Submit 100 HTTP requests.
        for (int i = 0; i < 100; i++) {
            String key = "user" + (i % 5 + 1);
            Future<?> future = executor.submit(() -> {




                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(
                                "http://localhost:8080/api/cache/" + key))
                        .header("X-API-Key", key)
                        .GET()
                        .build();

                // Start measuring immediately before sending request.
                long startTime = System.nanoTime();

                try {

                    HttpResponse<String> response =
                            client.send(
                                    request,
                                    HttpResponse.BodyHandlers.ofString()
                            );

                    long endTime = System.nanoTime();

                    long elapsedTime = endTime - startTime;

                    double milliseconds =
                            elapsedTime / 1_000_000.0;

                    if(response.body().contains("\"cacheHit\":true"))
                    {
                        cacheHits.incrementAndGet();
                    }
                    else{
                        cacheMisses.incrementAndGet();
                    }

                    latencies.add(milliseconds);
                    System.out.println(response.body());

                } catch (IOException | InterruptedException e) {

                    Thread.currentThread().interrupt();

                    throw new RuntimeException(e);
                }

                ;
            });

            futures.add(future);
        }


        // Wait for every request to finish.
        for (Future<?> future : futures) {
            future.get();
        }


        // All 100 requests have now finished.
        executor.shutdown();


        // Make sure we actually collected 100 measurements.
        System.out.println(
                "Number of measurements: " + latencies.size()
        );

        System.out.println("Cache hits: " + cacheHits.get());
        System.out.println("Cache misses: " + cacheMisses.get());


        // Sort from fastest → slowest.
        Collections.sort(latencies);


        // Calculate p50 and p99.
        double p50 = latencies.get(49);
        double p99 = latencies.get(98);

        double min = latencies.get(0);
        double max = latencies.get(99);

        System.out.println("Minimum latency: " + min);
        System.out.println("Maximum latency: " + max);

        System.out.println(
                "p50: " + p50 + " ms"
        );

        System.out.println(
                "p99: " + p99 + " ms"
        );
    }
}