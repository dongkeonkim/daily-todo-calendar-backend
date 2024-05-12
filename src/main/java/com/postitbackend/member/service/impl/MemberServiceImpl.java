package com.postitbackend.member.service.impl;

import com.postitbackend.member.dto.MemberDTO;
import com.postitbackend.member.dto.MemberUpdateDTO;
import com.postitbackend.member.entity.Member;
import com.postitbackend.member.repository.MemberRepository;
import com.postitbackend.member.service.MemberService;
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
    public MemberDTO findMember(MemberDTO memberDTO) {
        Optional<Member> member = memberRepository.findByEmailAndEnable(memberDTO.getEmail(), 1);
        return member.map(Member::toDTO).orElse(null);
    }

    @Override
    public void login(MemberDTO member, HttpServletRequest request) {
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
    public void createMember(MemberDTO memberDTO) {
        Optional<Member> optMember = memberRepository.findByEmail(memberDTO.getEmail());

        optMember.ifPresent(member -> {
            throw new RuntimeException();
        });

        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberDTO.setEnable(1);
        memberDTO.setRole("ROLE_USER");
        memberDTO.setRegDate(LocalDateTime.now());
        memberDTO.setUdtDate(LocalDateTime.now());

        try {
            memberRepository.save(memberDTO.toEntity());
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional
    public boolean updateMember(MemberDTO mDTO, MemberUpdateDTO memberUpdateDTO) {
        boolean result = false;
        Member member;
        Optional<Member> m = memberRepository.findByEmailAndEnable(mDTO.getEmail(), 1);

        if (m.isPresent()) {
            member = m.get();

            if (passwordEncoder.matches(memberUpdateDTO.getCurrentPassword(), member.getPassword())) {
                member.updatePassword(passwordEncoder.encode(memberUpdateDTO.getNewPassword()));
                result = true;
            }
        }

        return result;
    }

    @Override
    @Transactional
    public boolean deleteMember(MemberDTO memberDTO) {
        boolean result = false;
        Member member;
        Optional<Member> m = memberRepository.findByEmailAndEnable(memberDTO.getEmail(), 1);

        if (m.isPresent()) {
            member = m.get();
            if (passwordEncoder.matches(memberDTO.getPassword(), member.getPassword())) {
                member.changeEnable(0);
                result = true;
            }
        }

        return result;
    }
}
