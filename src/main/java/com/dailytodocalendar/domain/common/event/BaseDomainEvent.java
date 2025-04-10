package com.dailytodocalendar.domain.common.event;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

/** 기본 도메인 이벤트 구현체 */
@Getter
public abstract class BaseDomainEvent implements DomainEvent {

  private final UUID eventId;
  private final LocalDateTime occurredAt;

  protected BaseDomainEvent() {
    this.eventId = UUID.randomUUID();
    this.occurredAt = LocalDateTime.now();
  }

  @Override
  public LocalDateTime occurredAt() {
    return this.occurredAt;
  }
}
