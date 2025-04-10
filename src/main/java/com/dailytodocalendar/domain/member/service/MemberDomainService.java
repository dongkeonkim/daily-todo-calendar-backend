package com.dailytodocalendar.domain.member.service;

import com.dailytodocalendar.domain.common.event.DomainEventPublisher;
import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import com.dailytodocalendar.domain.member.model.MemberName;
import com.dailytodocalendar.domain.member.model.Password;
import lombok.RequiredArgsConstructor;

/** 회원 도메인 서비스 - 도메인 로직 중 단일 엔티티에 속하지 않는 비즈니스 로직 처리 */
@RequiredArgsConstructor
public class MemberDomainService {

  private final PasswordVerificationService passwordVerificationService;
  private final DomainEventPublisher eventPublisher;

  /**
   * 비밀번호 검증
   *
   * @param member 회원 엔티티
   * @param rawPassword 원본 비밀번호
   * @return 비밀번호 일치 여부
   */
  public boolean verifyPassword(Member member, String rawPassword) {
    if (rawPassword == null || rawPassword.isEmpty()) {
      throw new MemberDomainException("비밀번호는 필수 입력값입니다.");
    }
    return passwordVerificationService.verify(rawPassword, member.getPassword().getValue());
  }

  /**
   * 회원 정보 업데이트
   *
   * @param member 회원 엔티티
   * @param name 새 이름
   * @param newPassword 새 비밀번호 (변경하지 않을 경우 null)
   */
  public void updateMember(Member member, String name, String newPassword) {
    if (!member.isActive()) {
      throw new MemberDomainException("삭제된 회원의 정보는 수정할 수 없습니다.");
    }

    MemberName memberName = MemberName.of(name);
    Password encodedPassword = null;

    if (newPassword != null && !newPassword.trim().isEmpty()) {
      // 비밀번호 정책 검증
      Password.validateRawPassword(newPassword);

      // 비밀번호 인코딩
      String encoded = passwordVerificationService.encode(newPassword);
      encodedPassword = Password.of(encoded);
    }

    member.updateMember(memberName, encodedPassword, eventPublisher);
  }

  /**
   * 회원 탈퇴 처리
   *
   * @param member 회원 엔티티
   */
  public void withdrawMember(Member member) {
    if (!member.isActive()) {
      throw new MemberDomainException("이미 삭제된 회원입니다.");
    }

    member.markAsDeleted(eventPublisher);
  }

  /**
   * 새 회원 생성
   *
   * @param email 이메일
   * @param rawPassword 원본 비밀번호
   * @param name 이름
   * @return 생성된 회원 엔티티
   */
  public Member createMember(String email, String rawPassword, String name) {
    // 비밀번호 정책 검증
    Password.validateRawPassword(rawPassword);

    // 비밀번호 인코딩
    String encodedPassword = passwordVerificationService.encode(rawPassword);

    return Member.createNewMember(
        Email.of(email), Password.of(encodedPassword), MemberName.of(name), null);
  }
}
