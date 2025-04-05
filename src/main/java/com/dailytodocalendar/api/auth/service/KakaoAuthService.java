package com.dailytodocalendar.api.auth.service;

import com.dailytodocalendar.api.auth.dto.KakaoUserInfo;
import com.dailytodocalendar.api.auth.dto.LoginResponse;
import com.dailytodocalendar.api.member.entity.Member;
import com.dailytodocalendar.api.member.repository.MemberRepository;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.config.properties.KakaoProperties;
import com.dailytodocalendar.config.security.filter.JwtTokenProvider;
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
public class KakaoAuthService {

  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final KakaoProperties kakaoProperties;

  public LoginResponse processKakaoLogin(String code) {
    try {
      // 1. 액세스 토큰 받기
      String accessToken = getKakaoAccessToken(code);

      // 2. 사용자 정보 가져오기
      KakaoUserInfo userInfo = getKakaoUserInfo(accessToken);

      // 3. 회원가입 여부 확인 (없으면 자동 가입)
      Member member = registerKakaoUserIfNeeded(userInfo);

      // 4. JWT 토큰 생성
      String token =
          jwtTokenProvider.createToken(member.getId(), member.getEmail(), member.getRole());
      log.info("Kakao user logged in successfully: {}", member.getEmail());

      return LoginResponse.of(token);
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
    return memberRepository
        .findByEmailAndDelYn(userInfo.getEmail(), false)
        .orElseGet(
            () -> {
              // 회원이 없으면 새로 등록
              String randomPassword = UUID.randomUUID().toString();
              Member newMember =
                  Member.create(
                      userInfo.getEmail(), randomPassword, userInfo.getNickname(), passwordEncoder);
              newMember.setKakaoId(userInfo.getId());
              return memberRepository.save(newMember);
            });
  }

  /**
   * 카카오 계정 연결 해제 및 회원 탈퇴 처리
   *
   * @param memberId 탈퇴할 회원 ID
   */
  @Transactional
  public void unlinkKakaoAccount(Long memberId) {
    try {
      // 1. 회원 정보 조회
      Member member =
          memberRepository
              .findById(memberId)
              .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

      // 2. 카카오 ID가 있는 경우에만 연결 해제 진행
      if (member.getKakaoId() != null) {
        // 3. 카카오 연결 해제 API 호출
        unlinkFromKakao(member.getKakaoId());
      }

      // 4. 회원 정보 삭제 처리 (soft delete)
      member.changeDelYn(true);
      memberRepository.save(member);

      log.info("Successfully unlinked and deleted Kakao user: {}", member.getEmail());
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
