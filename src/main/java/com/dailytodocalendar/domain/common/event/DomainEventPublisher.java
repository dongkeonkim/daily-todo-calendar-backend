package com.dailytodocalendar.domain.common.event;

/** 도메인 이벤트 발행자 인터페이스 */
public interface DomainEventPublisher {

  /**
   * 도메인 이벤트 발행
   *
   * @param event 발행할 도메인 이벤트
   */
  void publish(DomainEvent event);
}
