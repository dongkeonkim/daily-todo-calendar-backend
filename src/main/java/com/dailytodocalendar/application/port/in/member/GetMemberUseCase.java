package com.dailytodocalendar.application.port.in.member;

import com.dailytodocalendar.application.port.in.member.dto.MemberInfoDto;

/** 회원 조회 유스케이스 - 인바운드 포트 */
public interface GetMemberUseCase {

  /**
   * 회원 정보 조회
   *
   * @param email 이메일
   * @return 회원 정보 DTO
   */
  MemberInfoDto getMemberByEmail(String email);
}
