package com.dailytodocalendar.application.port.in.member;

import com.dailytodocalendar.application.port.in.member.dto.UpdateMemberCommand;

/** 회원 정보 수정 유스케이스 - 인바운드 포트 */
public interface UpdateMemberUseCase {

  /**
   * 회원 정보 수정
   *
   * @param email 회원 이메일
   * @param command 수정 명령
   */
  void updateMember(String email, UpdateMemberCommand command);
}
