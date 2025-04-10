package com.dailytodocalendar.domain.auth.service;

import com.dailytodocalendar.domain.auth.model.AuthToken;
import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import com.dailytodocalendar.domain.member.model.MemberName;
import com.dailytodocalendar.domain.member.model.Password;
import com.dailytodocalendar.domain.member.service.PasswordVerificationService;
import lombok.RequiredArgsConstructor;

/** 인증 도메인 서비스 */
@RequiredArgsConstructor
public class AuthDomainService {

  private final PasswordVerificationService passwordVerificationService;
  private final TokenService tokenService;

  /**
   * 로그인 인증
   *
   * @param member 회원 엔티티
   * @param rawPassword 원본 비밀번호
   * @return 인증 토큰
   * @throws MemberDomainException 비밀번호가 일치하지 않을 경우
   */
  public AuthToken authenticate(Member member, String rawPassword) {
    if (!passwordVerificationService.verify(rawPassword, member.getPassword().getValue())) {
      throw new MemberDomainException("비밀번호가 일치하지 않습니다.");
    }

    return tokenService.createToken(member.getId(), member.getEmail().getValue(), member.getRole());
  }

  /**
   * 신규 회원 생성
   *
   * @param email 이메일
   * @param rawPassword 원본 비밀번호
   * @param name 이름
   * @return 새 회원 엔티티
   */
  public Member createNewMember(String email, String rawPassword, String name) {
    // 비밀번호 정책 검증
    Password.validateRawPassword(rawPassword);

    // 이메일 유효성 검증
    Email emailVO = Email.of(email);

    // 이름 유효성 검증
    MemberName nameVO = MemberName.of(name);

    // 비밀번호 인코딩
    String encodedPassword = passwordVerificationService.encode(rawPassword);

    return Member.createNewMember(emailVO, Password.of(encodedPassword), nameVO, null);
  }
}
