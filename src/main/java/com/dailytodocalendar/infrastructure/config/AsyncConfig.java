package com.dailytodocalendar.infrastructure.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** 비동기 처리 관련 설정 - 이벤트 처리 등의 비동기 작업 설정 */
@Configuration
@EnableAsync
public class AsyncConfig {

  /**
   * 도메인 이벤트 처리용 스레드 풀 설정
   *
   * @return 스레드 풀 실행기
   */
  @Bean(name = "domainEventTaskExecutor")
  public Executor domainEventTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(5);
    executor.setQueueCapacity(25);
    executor.setThreadNamePrefix("domain-event-");
    executor.initialize();
    return executor;
  }
}
