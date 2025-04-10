package com.dailytodocalendar.infrastructure.config.security.filter;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.infrastructure.config.security.custom.CustomUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/** JWT 토큰 생성 및 검증을 담당하는 컴포넌트 */
@Slf4j
@Component
public class JwtTokenProvider {

  private final Key key;
  private final long tokenValidityInMilliseconds;
  private final long refreshTokenValidityInMilliseconds;

  /**
   * 생성자
   *
   * @param secretKey JWT 비밀키
   * @param tokenValidity 토큰 유효시간 (밀리초)
   * @param refreshTokenValidity 리프레시 토큰 유효시간 (밀리초)
   */
  public JwtTokenProvider(
      @Value("${secret-key}") String secretKey,
      @Value("${jwt.expiration:7200000}") long tokenValidity,
      @Value("${jwt.refresh-expiration:604800000}") long refreshTokenValidity) {

    // HS512 알고리즘을 위한 시크릿 키 생성
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    // 키 길이가 적절한지 확인 (HS512는 512비트 = 64바이트 이상 필요)
    if (keyBytes.length < 64) {
      log.warn("시크릿 키 길이가 짧습니다. HS512 알고리즘에는 최소 64바이트가 필요합니다.");
    }
    this.key = Keys.hmacShaKeyFor(keyBytes);
    // 생성된 키의 알고리즘 확인
    log.info("생성된 JWT 키 정보: 알고리즘={}", ((SecretKey) key).getAlgorithm());

    this.tokenValidityInMilliseconds = tokenValidity;
    this.refreshTokenValidityInMilliseconds = refreshTokenValidity;

    log.info(
        "JWT 토큰 프로바이더 초기화 완료 - 토큰 유효시간: {}ms, 리프레시 토큰 유효시간: {}ms",
        tokenValidity,
        refreshTokenValidity);
  }

  /**
   * 사용자 인증 정보로 JWT 토큰 생성
   *
   * @param authentication 인증 정보
   * @return 생성된 JWT 토큰
   */
  public String createToken(Authentication authentication) {
    String authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity = new Date(now + this.tokenValidityInMilliseconds);

    String username = authentication.getName();
    Long userId = null;

    // CustomUser인 경우 userId 추출
    if (authentication.getPrincipal() instanceof CustomUser) {
      userId = ((CustomUser) authentication.getPrincipal()).getMemberId();
    }

    return Jwts.builder()
        .subject(username)
        .claim("auth", authorities)
        .claim("userId", userId)
        .signWith(key)
        .issuedAt(new Date())
        .expiration(validity)
        .compact();
  }

  /**
   * 사용자 정보로 JWT 토큰 생성
   *
   * @param userId 사용자 ID
   * @param username 사용자명
   * @param role 권한
   * @return 생성된 JWT 토큰
   */
  public String createToken(Long userId, String username, String role) {
    long now = (new Date()).getTime();
    Date validity = new Date(now + this.tokenValidityInMilliseconds);

    return Jwts.builder()
        .subject(username)
        .claim("auth", role)
        .claim("userId", userId)
        .signWith(key)
        .issuedAt(new Date())
        .expiration(validity)
        .compact();
  }

  /**
   * 리프레시 토큰 생성
   *
   * @param username 사용자명
   * @return 생성된 리프레시 토큰
   */
  public String createRefreshToken(String username) {
    long now = (new Date()).getTime();
    Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

    return Jwts.builder()
        .subject(username)
        .claim("type", "refresh")
        .signWith(key)
        .issuedAt(new Date())
        .expiration(validity)
        .compact();
  }

  /**
   * JWT 토큰에서 인증 정보 추출
   *
   * @param token JWT 토큰
   * @return 인증 정보
   */
  public Authentication getAuthentication(String token) {
    Claims claims = getAllClaimsFromToken(token);

    String username = claims.getSubject();
    Long userId = claims.get("userId", Long.class);

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get("auth").toString().split(","))
            .filter(auth -> !auth.trim().isEmpty())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    UserDetails principal;

    if (userId != null) {
      principal = new CustomUser(userId, username, "", authorities);
    } else {
      principal = new User(username, "", authorities);
    }

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  /**
   * JWT 토큰에서 모든 클레임 추출
   *
   * @param token JWT 토큰
   * @return 클레임
   */
  private Claims getAllClaimsFromToken(String token) {
    try {
      return Jwts.parser()
          .verifyWith((SecretKey) key)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (Exception e) {
      log.error("JWT 토큰 클레임 추출 실패: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.UNAUTHORIZED);
    }
  }

  /**
   * JWT 토큰 유효성 검증
   *
   * @param token JWT 토큰
   * @return 유효 여부
   */
  public boolean validateToken(String token) {
    System.out.println(key);
    try {
      Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.warn("만료된 JWT 토큰: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.TOKEN_EXPIRED);
    } catch (UnsupportedJwtException e) {
      log.warn("지원되지 않는 JWT 토큰: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.INVALID_TOKEN);
    } catch (MalformedJwtException e) {
      log.warn("유효하지 않은 JWT 토큰 형식: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.INVALID_TOKEN);
    } catch (SignatureException e) {
      log.warn("유효하지 않은 JWT 서명: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.INVALID_TOKEN);
    } catch (IllegalArgumentException e) {
      log.warn("빈 JWT 토큰: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.INVALID_TOKEN);
    } catch (Exception e) {
      log.warn("JWT 토큰 검증 실패: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.INVALID_TOKEN);
    }
  }

  /**
   * 토큰 만료 시간(밀리초) 반환
   *
   * @return 토큰 만료 시간(밀리초)
   */
  public long getTokenExpirationInMs() {
    return tokenValidityInMilliseconds;
  }

  /**
   * 토큰에서 이메일(username) 추출
   *
   * @param token JWT 토큰
   * @return 이메일
   */
  public String getEmailFromToken(String token) {
    Claims claims = getAllClaimsFromToken(token);
    return claims.getSubject();
  }
}
