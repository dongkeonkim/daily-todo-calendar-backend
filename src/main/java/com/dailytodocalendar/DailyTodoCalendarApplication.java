package com.dailytodocalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/** 헥사고날 아키텍처 기반 일정 관리 애플리케이션 */
@SpringBootApplication
@EntityScan("com.dailytodocalendar.adapter.out.persistence")
@EnableJpaRepositories("com.dailytodocalendar.adapter.out.persistence")
@EnableAsync
public class DailyTodoCalendarApplication {

  public static void main(String[] args) {
    SpringApplication.run(DailyTodoCalendarApplication.class, args);
  }
}
