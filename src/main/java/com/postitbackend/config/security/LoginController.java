package com.postitbackend.config.security;

import com.postitbackend.member.dto.MemberDTO;
import com.postitbackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final JwtProp jwtProp;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
//
//        String username = request.getUsername();
//        String password = request.getPassword();
//
//        log.info("username: " + username);
//        log.info("username: " + password);
//
//        List<String> roles = new ArrayList<>();
//        roles.add("ROLE_USER");
//
//        byte[] key = jwtProp.getSecretKey().getBytes();
//
//        // 토큰 생성
//        String jwt = Jwts.builder()
//                .signWith(Keys.hmacShaKeyFor(key), Jwts.SIG.HS512)
//                .header().add("typ", SecurityConstants.TOKEN_TYPE)
//                .and()
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5))
//                .claim("uid", username)
//                .claim("rol", roles)
//                .compact();
//
//        log.info("jwt : " + jwt);
//
//        return new ResponseEntity<String >(jwt, HttpStatus.OK);
//    }

    @Secured("ROLE_USER")
    @GetMapping("/user/info")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal MemberDTO memberDTO) {
        if (memberDTO != null) {
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
//    public ResponseEntity<?> userInfo(@RequestHeader(name="Authorization") String header) {
//
//        log.info("Authorization: " + header);
//
//        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "");
//
//        byte[] signKey = jwtProp.getSecretKey().getBytes();
//
//        // 토큰 해석
//        Jws<Claims> parsedToken = Jwts.parser()
//                .verifyWith(Keys.hmacShaKeyFor(signKey))
//                .build()
//                .parseSignedClaims(jwt);
//
//        String username = parsedToken.getPayload().get("uid").toString();
//        Object role = parsedToken.getPayload().get("rol");
//
//        log.info("Login: " + username + " role: " + role);
//
//        return new ResponseEntity<>(parsedToken.toString(), HttpStatus.OK);
//    }

    @Secured("ROLE_USER")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody MemberDTO memberDTO) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
