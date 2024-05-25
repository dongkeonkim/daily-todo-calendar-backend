package com.dailytodocalendar.member.dto;

import com.dailytodocalendar.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String role;
    private int enable;
    private LocalDateTime regDate;
    private LocalDateTime udtDate;

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .role(this.role)
                .enable(this.enable)
                .regDate(this.regDate)
                .udtDate(this.udtDate)
                .build();
    }

    public MemberSearchResult toMemberSearchResult() {
        return MemberSearchResult.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .role(this.role)
                .build();
    }

}
