package com.dailytodocalendar.config.security.filter;

import com.dailytodocalendar.api.member.dto.MemberDto;
import com.dailytodocalendar.api.member.repository.MemberRepository;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.config.security.constants.SecurityConstants;
import com.dailytodocalendar.config.security.custom.CustomUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final MemberRepository memberRepository;

  @Value("${secret-key}")
  private String secretKeyString;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    log.info("JWT Secret Key initialized");
  }

  public String createToken(long userId, String email, String roles) {
    Date now = new Date();

    Date validity = new Date(now.getTime() + 1000 * 60 * 60 * 2);

    String jwt =
        Jwts.builder()
            .signWith(secretKey, Jwts.SIG.HS512)
            .header()
            .add("typ", SecurityConstants.TOKEN_TYPE)
            .and()
            .issuedAt(now)
            .expiration(validity)
            .claim("uid", userId)
            .claim("email", email)
            .claim("role", roles)
            .compact();

    log.debug("JWT token created for user: {}", email);
    return jwt;
  }

  @Transactional(readOnly = true)
  public UsernamePasswordAuthenticationToken getAuthentication(String authHeader) {
    if (authHeader == null || authHeader.isEmpty()) {
      return null;
    }

    try {
      String jwt = authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
      Jws<Claims> parsedToken = parseToken(jwt);

      if (parsedToken == null) {
        return null;
      }

      Claims claims = parsedToken.getPayload();
      String email = claims.get("email", String.class);

      if (email == null) {
        log.warn("Email claim not found in token");
        return null;
      }

      MemberDto memberDto =
          memberRepository
              .findByEmailAndDelYn(email, false)
              .map(MemberDto::fromEntity)
              .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

      UserDetails userDetails = new CustomUser(memberDto);
      return new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());

    } catch (Exception e) {
      log.warn("Failed to authenticate with token: {}", e.getMessage());
      return null;
    }
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = parseToken(token);
      return !claims.getPayload().getExpiration().before(new Date());
    } catch (Exception e) {
      log.warn("Token validation failed: {}", e.getMessage());
      return false;
    }
  }

  private Jws<Claims> parseToken(String token) {
    try {
      return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    } catch (ExpiredJwtException e) {
      log.warn("Expired JWT token: {}", e.getMessage());
      throw e;
    } catch (UnsupportedJwtException e) {
      log.warn("Unsupported JWT token: {}", e.getMessage());
      throw e;
    } catch (MalformedJwtException e) {
      log.warn("Invalid JWT token: {}", e.getMessage());
      throw e;
    } catch (IllegalArgumentException e) {
      log.warn("Empty JWT claims string: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("JWT parsing error: {}", e.getMessage());
      throw e;
    }
  }
}
