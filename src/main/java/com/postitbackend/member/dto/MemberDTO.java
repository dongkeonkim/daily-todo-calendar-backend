package com.postitbackend.member.dto;

import com.postitbackend.config.security.UserAuth;
import com.postitbackend.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String role;
    private int enable;
    private LocalDateTime regDate;
    private LocalDateTime udtDate;

    List<UserAuth> authList;

    public MemberDTO createMemberDTO(String email, String password, String name, int enable, LocalDateTime regDate, LocalDateTime udtDate) {
        return MemberDTO.builder()
                .email(email)
                .password(password)
                .name(name)
                .enable(enable)
                .regDate(regDate)
                .udtDate(udtDate)
                .build();
    }

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

}
