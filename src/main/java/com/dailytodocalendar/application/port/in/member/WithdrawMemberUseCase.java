package com.dailytodocalendar.application.port.in.member;

import com.dailytodocalendar.application.port.in.member.dto.WithdrawMemberCommand;

/** 회원 탈퇴 유스케이스 - 인바운드 포트 */
public interface WithdrawMemberUseCase {

  /**
   * 회원 탈퇴
   *
   * @param command 탈퇴 명령
   */
  void withdrawMember(WithdrawMemberCommand command);
}
