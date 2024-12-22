package com.dailytodocalendar.api.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String role;

    private Boolean delYn;

    private LocalDateTime regDate;

    private LocalDateTime udtDate;

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
        this.udtDate = LocalDateTime.now();
    }

    public void changeDelYn(Boolean delYn) {
        this.delYn = delYn;
        this.udtDate = LocalDateTime.now();
    }

    public static Member create(String email, String password, String name, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.email = email;
        member.password = passwordEncoder.encode(password);
        member.name = name;
        member.role = "ROLE_USER";
        member.delYn = false;
        member.regDate = LocalDateTime.now();
        member.udtDate = LocalDateTime.now();
        return member;
    }

}
