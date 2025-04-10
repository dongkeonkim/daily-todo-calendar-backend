package com.dailytodocalendar.adapter.in.web.member;

import com.dailytodocalendar.adapter.in.web.member.dto.MemberUpdateRequest;
import com.dailytodocalendar.adapter.in.web.member.dto.MemberWithdrawRequest;
import com.dailytodocalendar.application.port.in.member.dto.UpdateMemberCommand;
import com.dailytodocalendar.application.port.in.member.dto.WithdrawMemberCommand;
import org.springframework.stereotype.Component;

/** 회원 DTO 매퍼 - 웹 요청 DTO와 애플리케이션 계층 명령 객체 간의 변환 */
@Component
public class MemberDtoMapper {

  /**
   * 회원 정보 수정 요청을 명령 객체로 변환
   *
   * @param request 회원 정보 수정 요청
   * @return 수정 명령 객체
   */
  public UpdateMemberCommand toUpdateMemberCommand(MemberUpdateRequest request) {
    return UpdateMemberCommand.builder()
        .name(request.name())
        .password(request.password())
        .newPassword(request.newPassword())
        .build();
  }

  /**
   * 회원 탈퇴 요청을 명령 객체로 변환
   *
   * @param request 회원 탈퇴 요청
   * @return 탈퇴 명령 객체
   */
  public WithdrawMemberCommand toWithdrawMemberCommand(MemberWithdrawRequest request) {
    return WithdrawMemberCommand.builder()
        .email(request.email())
        .password(request.password())
        .build();
  }
}
