package com.postitbackend.member.service.impl;

import com.postitbackend.member.dto.MemberDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public boolean findMember(MemberDTO dto) {
//        Member member = memberRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());
//        return member != null;
        return true;
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
    public MemberDTO createMember(MemberDTO memberDTO) {
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberDTO.setRole("ROLE_USER");

        Member member = memberRepository.save(memberDTO.toEntity());

        return member.toDTO();
    }

}
