package com.dailytodocalendar.infrastructure.config;

import com.dailytodocalendar.domain.auth.service.AuthDomainService;
import com.dailytodocalendar.domain.auth.service.TokenService;
import com.dailytodocalendar.domain.common.event.DomainEventPublisher;
import com.dailytodocalendar.domain.member.service.MemberDomainService;
import com.dailytodocalendar.domain.member.service.PasswordVerificationService;
import com.dailytodocalendar.domain.memo.service.MemoDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 도메인 서비스 빈 설정 - 도메인 서비스는 스프링에 직접 의존하지 않도록 빈으로 수동 등록 */
@Configuration
public class DomainConfig {

  /**
   * 회원 도메인 서비스 빈 등록
   *
   * @param passwordVerificationService 비밀번호 검증 서비스
   * @param domainEventPublisher 도메인 이벤트 발행자
   * @return 회원 도메인 서비스
   */
  @Bean
  public MemberDomainService memberDomainService(
      PasswordVerificationService passwordVerificationService,
      DomainEventPublisher domainEventPublisher) {
    return new MemberDomainService(passwordVerificationService, domainEventPublisher);
  }

  /**
   * 인증 도메인 서비스 빈 등록
   *
   * @param passwordVerificationService 비밀번호 검증 서비스
   * @param tokenService 토큰 서비스
   * @return 인증 도메인 서비스
   */
  @Bean
  public AuthDomainService authDomainService(
      PasswordVerificationService passwordVerificationService, TokenService tokenService) {
    return new AuthDomainService(passwordVerificationService, tokenService);
  }

  /**
   * 메모 도메인 서비스 빈 등록
   *
   * @return 메모 도메인 서비스
   */
  @Bean
  public MemoDomainService memoDomainService() {
    return new MemoDomainService();
  }
}
