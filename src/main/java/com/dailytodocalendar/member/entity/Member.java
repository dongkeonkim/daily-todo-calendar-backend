package com.dailytodocalendar.member.entity;

import com.dailytodocalendar.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_email", unique = true)
    private String email;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_role")
    private String role;

    @Column(name = "member_enable")
    private int enable;

    @Column(name = "member_reg_date")
    private LocalDateTime regDate;

    @Column(name = "member_udt_date")
    private LocalDateTime udtDate;

    public void updatePassword(String password) {
        this.password = password;
    }

    public void changeEnable(int enable) {
        this.enable = enable;
    }

    public MemberDto toDto() {
        return MemberDto.builder()
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

}
