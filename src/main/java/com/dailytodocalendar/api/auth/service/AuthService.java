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

  /**
   * 신규 사용자 등록
   *
   * @param signUpRequest
   */
  public void signUp(SignUpRequest signUpRequest) {
    log.debug("email(sign-up request): {}", signUpRequest.getEmail());

    memberRepository
        .findByEmailAndDelYn(signUpRequest.getEmail(), false)
        .ifPresent(
            m -> {
              log.warn("Sign-up failed: Email already exists: {}", signUpRequest.getEmail());
              throw new ApplicationException(ErrorCode.USER_ALREADY_EXIST);
            });

    try {
      Member newMember =
          Member.create(
              signUpRequest.getEmail(),
              signUpRequest.getPassword(),
              signUpRequest.getName(),
              passwordEncoder);
      memberRepository.save(newMember);
      log.info("User registered successfully: {}", signUpRequest.getEmail());
    } catch (Exception e) {
      log.error("Error during user registration: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.DATABASE_ERROR);
    }
  }

  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest loginRequest) {
    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    log.debug("email(login request): {}", email);

    Member member =
        memberRepository
            .findByEmailAndDelYn(email, false)
            .orElseThrow(
                () -> {
                  log.warn("Login failed: User not found or deleted: {}", email);
                  return new ApplicationException(ErrorCode.INVALID_LOGIN);
                });

    if (!passwordEncoder.matches(password, member.getPassword())) {
      log.warn("Login failed: Invalid password for user: {}", email);
      throw new ApplicationException(ErrorCode.INVALID_LOGIN);
    }

    try {
      String token =
          jwtTokenProvider.createToken(member.getId(), member.getEmail(), member.getRole());
      log.info("User logged in successfully: {}", email);
      return LoginResponse.of(token);
    } catch (Exception e) {
      log.error("Error generating token for user {}: {}", email, e.getMessage());
      throw new ApplicationException(ErrorCode.INVALID_LOGIN);
    }
  }
}
