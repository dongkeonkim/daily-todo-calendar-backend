package com.dailytodocalendar.config;

import java.util.Arrays;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  @Profile("dev")
  public CacheManager devCacheManager() {
    ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
    cacheManager.setCacheNames(
        Arrays.asList("todoYearsCache", "calendarDataCache", "userProfileCache"));
    return cacheManager;
  }
}
