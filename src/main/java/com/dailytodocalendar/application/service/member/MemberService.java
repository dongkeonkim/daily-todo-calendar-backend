package com.dailytodocalendar.application.service.member;

import com.dailytodocalendar.application.port.in.member.GetMemberUseCase;
import com.dailytodocalendar.application.port.in.member.UpdateMemberUseCase;
import com.dailytodocalendar.application.port.in.member.WithdrawMemberUseCase;
import com.dailytodocalendar.application.port.in.member.dto.MemberInfoDto;
import com.dailytodocalendar.application.port.in.member.dto.UpdateMemberCommand;
import com.dailytodocalendar.application.port.in.member.dto.WithdrawMemberCommand;
import com.dailytodocalendar.application.port.out.member.LoadMemberPort;
import com.dailytodocalendar.application.port.out.member.SaveMemberPort;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import com.dailytodocalendar.domain.member.service.MemberDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 회원 서비스 - 애플리케이션 서비스로 유스케이스 구현 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService implements GetMemberUseCase, UpdateMemberUseCase, WithdrawMemberUseCase {

  private final LoadMemberPort loadMemberPort;
  private final SaveMemberPort saveMemberPort;
  private final MemberDomainService memberDomainService;

  /**
   * 회원 정보 조회
   *
   * @param email 이메일
   * @return 회원 정보 DTO
   */
  @Override
  @Transactional(readOnly = true)
  public MemberInfoDto getMemberByEmail(String email) {
    try {
      Member member = getMemberByEmailAndNotDeleted(email);
      return MemberInfoDto.fromDomain(member);
    } catch (MemberDomainException e) {
      log.error("회원 도메인 예외 발생: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.USER_NOT_FOUND, e);
    }
  }

  /**
   * 회원 정보 수정
   *
   * @param email 이메일
   * @param command 수정 명령
   */
  @Override
  public void updateMember(String email, UpdateMemberCommand command) {
    try {
      Member member = getMemberByEmailAndNotDeleted(email);

      // 카카오 회원이 아닌 경우 비밀번호 검증
      if (!member.isKakaoMember()) {
        if (!memberDomainService.verifyPassword(member, command.password())) {
          throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
      }

      memberDomainService.updateMember(member, command.name(), command.newPassword());
      saveMemberPort.save(member);

      log.info("회원 정보가 업데이트되었습니다. email: {}", email);
    } catch (MemberDomainException e) {
      log.error("회원 정보 수정 중 도메인 예외 발생: {}", e.getMessage());
      throw translateDomainException(e);
    }
  }

  /**
   * 회원 탈퇴
   *
   * @param command 탈퇴 명령
   */
  @Override
  public void withdrawMember(WithdrawMemberCommand command) {
    try {
      Member member = getMemberByEmailAndNotDeleted(command.email());

      // 소셜 로그인 회원(카카오 ID가 있는 경우)은 소셜 탈퇴 API를 통해서만 탈퇴 가능
      if (member.isKakaoMember()) {
        throw new ApplicationException(ErrorCode.INVALID_REQUEST);
      }

      if (!memberDomainService.verifyPassword(member, command.password())) {
        throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
      }

      memberDomainService.withdrawMember(member);
      saveMemberPort.save(member);

      log.info("회원이 탈퇴 처리되었습니다. email: {}", command.email());
    } catch (MemberDomainException e) {
      log.error("회원 탈퇴 중 도메인 예외 발생: {}", e.getMessage());
      throw translateDomainException(e);
    }
  }

  /**
   * 이메일로 회원 조회 (미삭제 상태)
   *
   * @param email 이메일
   * @return 회원 엔티티
   * @throws ApplicationException 회원을 찾을 수 없을 때
   */
  private Member getMemberByEmailAndNotDeleted(String email) {
    try {
      Email emailVO = Email.of(email);
      return loadMemberPort
          .loadByEmailAndDelYn(emailVO, false)
          .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    } catch (MemberDomainException e) {
      log.error("유효하지 않은 이메일 형식: {}", email);
      throw new ApplicationException(ErrorCode.INVALID_REQUEST, e);
    }
  }

  /**
   * 도메인 예외를 애플리케이션 예외로 변환
   *
   * @param e 도메인 예외
   * @return 변환된 애플리케이션 예외
   */
  private ApplicationException translateDomainException(MemberDomainException e) {
    // 예외 메시지 분석하여 적절한 애플리케이션 예외로 변환
    String message = e.getMessage();

    if (message.contains("비밀번호")) {
      return new ApplicationException(ErrorCode.PASSWORD_POLICY_VIOLATION, e);
    } else if (message.contains("이메일")) {
      return new ApplicationException(ErrorCode.INVALID_REQUEST, e);
    } else if (message.contains("이름")) {
      return new ApplicationException(ErrorCode.VALIDATION_ERROR, e);
    } else if (message.contains("삭제된 회원")) {
      return new ApplicationException(ErrorCode.USER_NOT_FOUND, e);
    }

    return new ApplicationException(ErrorCode.INVALID_REQUEST, e);
  }
}
