package com.dailytodocalendar.application.port.out.member;

import com.dailytodocalendar.domain.member.model.Member;

/** 회원 저장 포트 - 아웃바운드 포트 */
public interface SaveMemberPort {

  /**
   * 회원 저장/수정
   *
   * @param member 회원 엔티티
   * @return 저장된 회원 엔티티
   */
  Member save(Member member);
}
