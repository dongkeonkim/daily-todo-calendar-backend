package com.dailytodocalendar.domain.member.service;

import com.dailytodocalendar.domain.member.model.Member;

/** 회원 도메인 서비스 인터페이스 */
public interface MemberService {

  /**
   * 이메일로 회원 조회
   *
   * @param email 이메일
   * @return 회원
   */
  Member getMemberByEmail(String email);

  /**
   * ID로 회원 조회
   *
   * @param id 회원 ID
   * @return 회원
   */
  Member getMemberById(Long id);

  /**
   * 회원 생성
   *
   * @param member 회원
   * @return 생성된 회원
   */
  Member createMember(Member member);

  /**
   * 회원 탈퇴
   *
   * @param email 이메일
   */
  void withdrawMember(String email);
}
