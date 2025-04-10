package com.dailytodocalendar.domain.member.event;

import com.dailytodocalendar.domain.common.event.BaseDomainEvent;
import lombok.Getter;

/** 회원 관련 도메인 이벤트 기본 클래스 */
@Getter
public abstract class MemberEvent extends BaseDomainEvent {

  private final Long memberId;
  private final String email;

  protected MemberEvent(Long memberId, String email) {
    super();
    this.memberId = memberId;
    this.email = email;
  }
}
