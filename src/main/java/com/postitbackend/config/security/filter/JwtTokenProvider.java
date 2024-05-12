package com.postitbackend.config.security.filter;

import com.postitbackend.config.security.custom.CustomUser;
import com.postitbackend.config.security.constants.SecurityConstants;
import com.postitbackend.member.dto.MemberDTO;
import com.postitbackend.member.entity.Member;
import com.postitbackend.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final MemberRepository memberRepository;

    @Value("${secret-key}")
    private String secretKey;

    public String createToken(long uuid, String email, String roles) {
        String jwt =  Jwts.builder()
                .signWith(getShaKey(), Jwts.SIG.HS512)
                .header().add("typ", SecurityConstants.TOKEN_TYPE)
                .and()
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5))
                .claim("uid", uuid)
                .claim("email", email)
                .claim("role", roles)
                .compact();

        log.info("jwt : " + jwt);

        return jwt;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }

        try {
            String jwt = authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("parsedToken: " + parsedToken);

            String email = parsedToken.getPayload().get("email").toString();
            log.info("email: " + email);

            Claims claims = parsedToken.getPayload();
            Object roles = claims.get("role");
            log.info("roles: " + roles);

            MemberDTO memberDTO = new MemberDTO();

            try {
                Optional<Member> member = memberRepository.findByEmailAndEnable(email, 1);
                member.ifPresent(m -> {
                    memberDTO.setId(m.getId());
                    memberDTO.setRole(m.getRole());
                    memberDTO.setEmail(m.getEmail());
                    memberDTO.setPassword(m.getPassword());
                });
            } catch (Exception e) {
                log.error("토큰 유효 -> DB 추가 정보 조회시 에러 발생");
            }

            UserDetails userDetails = new CustomUser(memberDTO);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (ExpiredJwtException exception) {
            log.warn("expired JWT: {} failed: {}" , authHeader, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("unsupported JWT: {} failed: {}" , authHeader, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("invalid JWT: {} failed: {}" , authHeader, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("empty JWT: {} failed: {}" , authHeader, exception.getMessage());
        }

        return null;
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String jwt) {
        try {
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            Date exp = parsedToken.getPayload().getExpiration();
            return !exp.before(new Date());
        } catch (ExpiredJwtException exception) {
            log.warn("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.warn("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.warn("Token is null");
            return false;
        } catch (Exception exception) {
            return false;
        }

    }

    private byte[] getSignKey() {
        return secretKey.getBytes();
    }

    private SecretKey getShaKey() {
        return Keys.hmacShaKeyFor(getSignKey());
    }

}
