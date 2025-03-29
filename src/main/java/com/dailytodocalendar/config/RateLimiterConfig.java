package com.dailytodocalendar.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Profile("prod")
public class RateLimiterConfig {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String clientId) {
        return buckets.computeIfAbsent(clientId, key -> createNewBucket());
    }

    private Bucket createNewBucket() {
        Bandwidth minuteLimit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        Bandwidth secondLimit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofSeconds(1)));

        return Bucket.builder()
                .addLimit(minuteLimit)
                .addLimit(secondLimit)
                .build();
    }

    @Bean
    public BucketCleanupJob bucketCleanupJob() {
        return new BucketCleanupJob(buckets);
    }

    public static class BucketCleanupJob {
        private final Map<String, Bucket> buckets;
        private final Map<String, Long> lastAccessTimes = new ConcurrentHashMap<>();

        public BucketCleanupJob(Map<String, Bucket> buckets) {
            this.buckets = buckets;

            Thread cleanupThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(Duration.ofHours(1).toMillis());
                        cleanup();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            cleanupThread.setDaemon(true);
            cleanupThread.start();
        }

        public void recordAccess(String clientId) {
            lastAccessTimes.put(clientId, System.currentTimeMillis());
        }

        private void cleanup() {
            long cutoffTime = System.currentTimeMillis() - Duration.ofHours(1).toMillis();

            lastAccessTimes.entrySet().removeIf(entry -> {
                if (entry.getValue() < cutoffTime) {
                    buckets.remove(entry.getKey());
                    return true;
                }
                return false;
            });
        }
    }
}