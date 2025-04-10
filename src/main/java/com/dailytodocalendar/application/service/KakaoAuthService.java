package com.dailytodocalendar.application.service;

import com.dailytodocalendar.adapter.in.web.auth.kakao.dto.KakaoUserInfo;
import com.dailytodocalendar.application.port.in.auth.KakaoAuthUseCase;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.common.util.SecurityUtils;
import com.dailytodocalendar.domain.auth.model.AuthToken;
import com.dailytodocalendar.domain.auth.service.TokenService;
import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import com.dailytodocalendar.domain.member.model.MemberName;
import com.dailytodocalendar.domain.member.model.Password;
import com.dailytodocalendar.domain.member.service.MemberService;
import com.dailytodocalendar.infrastructure.config.properties.KakaoProperties;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoAuthService implements KakaoAuthUseCase {

  private final MemberService memberService;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;
  private final KakaoProperties kakaoProperties;

  @Override
  public AuthToken processKakaoLogin(String code) {
    try {
      // 1. 액세스 토큰 받기
      String accessToken = getKakaoAccessToken(code);

      // 2. 사용자 정보 가져오기
      KakaoUserInfo userInfo = getKakaoUserInfo(accessToken);

      // 3. 회원가입 여부 확인 (없으면 자동 가입)
      Member member = registerKakaoUserIfNeeded(userInfo);

      // 4. JWT 토큰 생성
      AuthToken token =
          tokenService.createToken(member.getId(), member.getEmail().getValue(), member.getRole());
      log.info("Kakao user logged in successfully: {}", member.getEmail());

      return token;
    } catch (Exception e) {
      log.error("Kakao login error: {}", e.getMessage(), e);
      throw new ApplicationException(ErrorCode.EXTERNAL_SERVICE_ERROR);
    }
  }

  private String getKakaoAccessToken(String code) {
    try {
      // HTTP 요청을 통해 카카오 액세스 토큰 받기
      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("grant_type", "authorization_code");
      params.add("client_id", kakaoProperties.getClientId());
      params.add("redirect_uri", kakaoProperties.getRedirectUri());
      params.add("code", code);
      params.add("client_secret", kakaoProperties.getClientSecret());

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

      ResponseEntity<Map> response =
          restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, Map.class);

      return (String) response.getBody().get("access_token");
    } catch (Exception e) {
      log.error("Failed to get Kakao access token: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.EXTERNAL_SERVICE_ERROR);
    }
  }

  private KakaoUserInfo getKakaoUserInfo(String accessToken) {
    try {
      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(accessToken);
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

      ResponseEntity<Map> response =
          restTemplate.exchange(
              "https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, Map.class);

      Map<String, Object> body = response.getBody();
      Map<String, Object> properties = (Map<String, Object>) body.get("properties");
      Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");

      return KakaoUserInfo.builder()
          .id(Long.valueOf(body.get("id").toString()))
          .nickname((String) properties.get("nickname"))
          .email((String) kakaoAccount.get("email"))
          .build();
    } catch (Exception e) {
      log.error("Failed to get Kakao user info: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.EXTERNAL_SERVICE_ERROR);
    }
  }

  private Member registerKakaoUserIfNeeded(KakaoUserInfo userInfo) {
    // 이메일로 회원 조회
    try {
      return memberService.getMemberByEmail(userInfo.email());
    } catch (ApplicationException e) {
      if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
        // 회원이 없으면 새로 등록
        String randomPassword = UUID.randomUUID().toString();
        Member newMember =
            Member.createNewMember(
                Email.of(userInfo.email()),
                Password.of(passwordEncoder.encode(randomPassword)),
                MemberName.of(userInfo.nickname()),
                userInfo.id());

        return memberService.createMember(newMember);
      }
      throw e;
    }
  }

  @Override
  public void unlinkKakaoAccount() {
    try {
      // 현재 인증된 사용자 정보 가져오기
      Long memberId = SecurityUtils.getCurrentUserId();
      String email = SecurityUtils.getCurrentUserEmail();

      // 회원 정보 조회
      Member member = memberService.getMemberById(memberId);

      // 카카오 ID가 있는 경우에만 연결 해제 진행
      if (member.getKakaoId() != null) {
        // 카카오 연결 해제 API 호출
        unlinkFromKakao(member.getKakaoId());
      }

      // 회원 탈퇴 처리
      memberService.withdrawMember(email);

      log.info("Successfully unlinked and deleted Kakao user: {}", email);
    } catch (Exception e) {
      log.error("Failed to unlink Kakao account: {}", e.getMessage(), e);
      throw new ApplicationException(ErrorCode.SERVER_ERROR);
    }
  }

  /**
   * 카카오 연결 해제 API 호출
   *
   * @param kakaoId 연결 해제할 카카오 ID
   */
  private void unlinkFromKakao(Long kakaoId) {
    try {
      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      headers.set("Authorization", "KakaoAK " + kakaoProperties.getAdminKey());

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("target_id_type", "user_id");
      params.add("target_id", kakaoId.toString());

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

      restTemplate.postForEntity("https://kapi.kakao.com/v1/user/unlink", request, Map.class);

      log.info("Successfully unlinked Kakao ID: {}", kakaoId);
    } catch (Exception e) {
      log.error("Error unlinking from Kakao: {}", e.getMessage(), e);
    }
  }
}
