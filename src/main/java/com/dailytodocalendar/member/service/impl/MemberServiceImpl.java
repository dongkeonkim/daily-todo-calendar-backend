package com.dailytodocalendar.member.service.impl;

import com.dailytodocalendar.member.dto.MemberDto;
import com.dailytodocalendar.member.dto.MemberUpdateDto;
import com.dailytodocalendar.member.entity.Member;
import com.dailytodocalendar.member.repository.MemberRepository;
import com.dailytodocalendar.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public MemberDto findMember(MemberDto memberDto) {
        Optional<Member> member = memberRepository.findByEmailAndEnable(memberDto.getEmail(), 1);
        return member.map(Member::toDto).orElse(null);
    }

    @Override
    public void login(MemberDto member, HttpServletRequest request) {
        String username = member.getEmail();
        String password = member.getPassword();

        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        token.setDetails(new WebAuthenticationDetails(request));

        // 인증 시도
        Authentication authentication = authenticationManager.authenticate(token);

        log.info("로그인 요청 아이디: " + username);
        log.info("인증 여부: " + authentication.isAuthenticated());

        // 시큐리티에 해당 인증 정보 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    @Transactional
    public void createMember(MemberDto memberDto) {
        Optional<Member> optMember = memberRepository.findByEmail(memberDto.getEmail());

        optMember.ifPresent(member -> {
            throw new RuntimeException();
        });

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberDto.setEnable(1);
        memberDto.setRole("ROLE_USER");
        memberDto.setRegDate(LocalDateTime.now());
        memberDto.setUdtDate(LocalDateTime.now());

        try {
            memberRepository.save(memberDto.toEntity());
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional
    public boolean updateMember(MemberDto mDto, MemberUpdateDto memberUpdateDto) {
        boolean result = false;
        Member member;
        Optional<Member> m = memberRepository.findByEmailAndEnable(mDto.getEmail(), 1);

        if (m.isPresent()) {
            member = m.get();

            if (passwordEncoder.matches(memberUpdateDto.getCurrentPassword(), member.getPassword())) {
                member.updatePassword(passwordEncoder.encode(memberUpdateDto.getNewPassword()));
                result = true;
            }
        }

        return result;
    }

    @Override
    @Transactional
    public boolean deleteMember(MemberDto memberDto) {
        boolean result = false;
        Member member;
        Optional<Member> m = memberRepository.findByEmailAndEnable(memberDto.getEmail(), 1);

        if (m.isPresent()) {
            member = m.get();
            if (passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
                member.changeEnable(0);
                result = true;
            }
        }

        return result;
    }
}
