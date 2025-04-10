package com.dailytodocalendar.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** JPA 관련 설정 */
@Configuration
public class JpaConfig {

  @PersistenceContext private EntityManager em;

  /** QueryDSL 사용을 위한 JPAQueryFactory 빈 등록 */
  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(em);
  }
}
