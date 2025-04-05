package com.dailytodocalendar.api.member.service;

import com.dailytodocalendar.api.member.dto.MemberDeleteDto;
import com.dailytodocalendar.api.member.dto.MemberDto;
import com.dailytodocalendar.api.member.dto.MemberUpdateDto;
import com.dailytodocalendar.api.member.entity.Member;
import com.dailytodocalendar.api.member.repository.MemberRepository;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public MemberDto findMember(MemberDto memberDto) {
    return memberRepository
        .findByEmailAndDelYn(memberDto.getEmail(), false)
        .map(MemberDto::fromEntity)
        .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
  }

  @Transactional
  public void updateMember(MemberDto mDto, MemberUpdateDto memberUpdateDto) {
    Member member =
        memberRepository
            .findByEmailAndDelYn(mDto.getEmail(), false)
            .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

    // 카카오 사용자(kakaoId가 있는 경우)는 비밀번호 검증을 건너뜀
    if (member.getKakaoId() == null) {
      if (!passwordEncoder.matches(memberUpdateDto.getPassword(), member.getPassword())) {
        throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
      }
    }

    member.updateMember(memberUpdateDto, passwordEncoder);
  }

  public void deleteMember(MemberDeleteDto memberDeleteDto) {
    Member member =
        memberRepository
            .findByEmailAndDelYn(memberDeleteDto.getEmail(), false)
            .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

    // 소셜 로그인 회원(카카오 ID가 있는 경우)은 소셜 탈퇴 API를 통해서만 탈퇴 가능
    if (member.getKakaoId() != null) {
      throw new ApplicationException(ErrorCode.INVALID_REQUEST);
    }

    if (!passwordEncoder.matches(memberDeleteDto.getPassword(), member.getPassword())) {
      throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
    }

    member.changeDelYn(true);
  }
}
