package com.dailytodocalendar.domain.common.event;

import java.time.LocalDateTime;

/** 도메인 이벤트 기본 인터페이스 - 도메인에서 발생하는 중요한 변경사항에 대한 이벤트 */
public interface DomainEvent {

  /**
   * 이벤트 발생 시간
   *
   * @return 이벤트 발생 시간
   */
  LocalDateTime occurredAt();
}
