package com.dailytodocalendar.domain.member.event;

import lombok.Getter;

/** 회원 삭제 이벤트 */
@Getter
public class MemberDeletedEvent extends MemberEvent {

  public MemberDeletedEvent(Long memberId, String email) {
    super(memberId, email);
  }
}
