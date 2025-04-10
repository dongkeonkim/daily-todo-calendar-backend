package com.dailytodocalendar.adapter.out.persistence.member;

import com.dailytodocalendar.application.port.out.member.LoadMemberPort;
import com.dailytodocalendar.application.port.out.member.SaveMemberPort;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import com.dailytodocalendar.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberServiceAdapter implements MemberService {

  private final LoadMemberPort loadMemberPort;
  private final SaveMemberPort saveMemberPort;

  @Override
  public Member getMemberByEmail(String email) {
    try {
      Email emailVO = Email.of(email);
      return loadMemberPort
          .loadByEmailAndDelYn(emailVO, false)
          .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    } catch (Exception e) {
      log.error("회원 조회 중 오류 발생: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
    }
  }

  @Override
  public Member getMemberById(Long id) {
    return loadMemberPort
        .loadById(id)
        .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public Member createMember(Member member) {
    try {
      return saveMemberPort.save(member);
    } catch (Exception e) {
      log.error("회원 생성 중 오류 발생: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.DATABASE_ERROR);
    }
  }

  @Override
  public void withdrawMember(String email) {
    try {
      Member member = getMemberByEmail(email);
      member.markAsDeleted(null);
      saveMemberPort.save(member);
    } catch (ApplicationException e) {
      if (e.getErrorCode() != ErrorCode.USER_NOT_FOUND) {
        throw e;
      }
      log.warn("이미 탈퇴한 회원입니다: {}", email);
    } catch (Exception e) {
      log.error("회원 탈퇴 중 오류 발생: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.DATABASE_ERROR);
    }
  }
}
