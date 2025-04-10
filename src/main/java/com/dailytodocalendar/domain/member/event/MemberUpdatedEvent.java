package com.dailytodocalendar.domain.member.event;

import lombok.Getter;

/** 회원 정보 업데이트 이벤트 */
@Getter
public class MemberUpdatedEvent extends MemberEvent {

  public MemberUpdatedEvent(Long memberId, String email) {
    super(memberId, email);
  }
}
