package com.dailytodocalendar.infrastructure.event;

import com.dailytodocalendar.domain.common.event.DomainEvent;
import com.dailytodocalendar.domain.common.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/** 스프링 이벤트 시스템 기반 도메인 이벤트 발행자 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpringApplicationEventPublisher implements DomainEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void publish(DomainEvent event) {
    log.debug("도메인 이벤트 발행: {}", event.getClass().getSimpleName());
    applicationEventPublisher.publishEvent(event);
  }
}
