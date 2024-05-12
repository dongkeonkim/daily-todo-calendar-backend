package com.postitbackend.member.entity;

import com.postitbackend.member.dto.MemberDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String password;
    private String name;
    private String role;
    private int enable;
    private LocalDateTime regDate;
    private LocalDateTime udtDate;

    public void updatePassword(String password) {
        this.password = password;
    }

    public void changeEnable(int enable) {
        this.enable = enable;
    }

    public MemberDTO toDTO() {
        return MemberDTO.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .role(this.role)
                .enable(this.enable)
                .regDate(this.regDate)
                .udtDate(this.udtDate)
                .build();
    }

    @Builder
    public Member(String email, String password, String name, String role, int enable, LocalDateTime regDate, LocalDateTime udtDate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.enable = enable;
        this.regDate = regDate;
        this.udtDate = udtDate;
    }

}
