package com.dailytodocalendar.application.service.auth;

import com.dailytodocalendar.application.port.in.auth.LoginUseCase;
import com.dailytodocalendar.application.port.in.auth.SignUpUseCase;
import com.dailytodocalendar.application.port.in.auth.dto.LoginCommand;
import com.dailytodocalendar.application.port.in.auth.dto.LoginResult;
import com.dailytodocalendar.application.port.in.auth.dto.SignUpCommand;
import com.dailytodocalendar.application.port.out.member.LoadMemberPort;
import com.dailytodocalendar.application.port.out.member.SaveMemberPort;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.domain.auth.model.AuthToken;
import com.dailytodocalendar.domain.auth.service.AuthDomainService;
import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 인증 애플리케이션 서비스 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService implements LoginUseCase, SignUpUseCase {

  private final LoadMemberPort loadMemberPort;
  private final SaveMemberPort saveMemberPort;
  private final AuthDomainService authDomainService;

  /**
   * 회원가입 처리
   *
   * @param command 회원가입 명령
   */
  @Override
  public void signUp(SignUpCommand command) {
    try {
      command.validate();

      // 이메일 중복 확인
      Email email = Email.of(command.email());
      loadMemberPort
          .loadByEmailAndDelYn(email, false)
          .ifPresent(
              m -> {
                log.warn("회원가입 실패: 이미 존재하는 이메일: {}", command.email());
                throw new ApplicationException(ErrorCode.USER_ALREADY_EXIST);
              });

      // 새 회원 생성
      Member newMember =
          authDomainService.createNewMember(command.email(), command.password(), command.name());

      // 회원 저장
      saveMemberPort.save(newMember);
      log.info("회원가입 성공: {}", command.email());

    } catch (MemberDomainException e) {
      log.error("회원가입 도메인 오류: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.VALIDATION_ERROR, e);
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      log.error("회원가입 중 오류 발생: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.SERVER_ERROR, e);
    }
  }

  /**
   * 로그인 처리
   *
   * @param command 로그인 명령
   * @return 로그인 결과
   */
  @Override
  @Transactional(readOnly = true)
  public LoginResult login(LoginCommand command) {
    try {
      command.validate();

      // 회원 조회
      Member member =
          loadMemberPort
              .loadByEmailAndDelYn(Email.of(command.email()), false)
              .orElseThrow(
                  () -> {
                    log.warn("로그인 실패: 존재하지 않는 이메일: {}", command.email());
                    return new ApplicationException(ErrorCode.INVALID_LOGIN);
                  });

      // 인증 처리
      AuthToken token = authDomainService.authenticate(member, command.password());
      log.info("로그인 성공: {}", command.email());

      return LoginResult.of(token.getValue());

    } catch (MemberDomainException e) {
      log.error("로그인 도메인 오류: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.INVALID_LOGIN, e);
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      log.error("로그인 중 오류 발생: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.SERVER_ERROR, e);
    }
  }
}
