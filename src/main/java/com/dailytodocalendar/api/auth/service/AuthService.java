package com.dailytodocalendar.api.auth.service;

import com.dailytodocalendar.api.auth.dto.LoginRequest;
import com.dailytodocalendar.api.auth.dto.LoginResponse;
import com.dailytodocalendar.api.auth.dto.SignUpRequest;
import com.dailytodocalendar.api.member.entity.Member;
import com.dailytodocalendar.api.member.repository.MemberRepository;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.config.security.filter.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest SignUpRequest) {
        memberRepository.findByEmail(SignUpRequest.getEmail())
                .ifPresent(m -> { throw new ApplicationException(ErrorCode.USER_ALREADY_EXIST); });

        memberRepository.save(Member.create(
                SignUpRequest.getEmail(), SignUpRequest.getPassword(), SignUpRequest.getName(),
                passwordEncoder
        ));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Member member = memberRepository.findByEmailAndDelYn(email, false)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_LOGIN));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_LOGIN);
        }

        String token = jwtTokenProvider.createToken(member.getId(), member.getEmail(), member.getRole());
        return LoginResponse.of(token);
    }

}
