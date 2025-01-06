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
        return memberRepository.findByEmailAndDelYn(memberDto.getEmail(), false)
                .map(MemberDto::fromEntity)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateMember(MemberDto mDto, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findByEmailAndDelYn(mDto.getEmail(), false)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(memberUpdateDto.getPassword(), member.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        member.updateMember(memberUpdateDto, passwordEncoder);
    }

    @Transactional
    public void deleteMember(MemberDeleteDto memberDeleteDto) {
        Member member = memberRepository.findByEmailAndDelYn(memberDeleteDto.getEmail(), false)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(memberDeleteDto.getPassword(), member.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        member.changeDelYn(true);
    }
}
