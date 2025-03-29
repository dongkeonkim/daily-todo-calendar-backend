package com.dailytodocalendar.api.member.dto;

import com.dailytodocalendar.api.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

  private Long id;
  private String email;
  @JsonIgnore private String password;
  private String name;
  private String role;
  private Boolean delYn;
  private LocalDateTime regDate;
  private LocalDateTime udtDate;

  public static MemberDto of(
      Long id,
      String email,
      String password,
      String name,
      String role,
      Boolean delYn,
      LocalDateTime regDate,
      LocalDateTime udtDate) {
    MemberDto memberDto = new MemberDto();
    memberDto.id = id;
    memberDto.email = email;
    memberDto.password = password;
    memberDto.name = name;
    memberDto.role = role;
    memberDto.delYn = delYn;
    memberDto.regDate = regDate;
    memberDto.udtDate = udtDate;
    return memberDto;
  }

  public static MemberDto fromEntity(Member member) {
    return of(
        member.getId(),
        member.getEmail(),
        member.getPassword(),
        member.getName(),
        member.getRole(),
        member.getDelYn(),
        member.getRegDate(),
        member.getUdtDate());
  }

  @Override
  public String toString() {
    return "MemberDto{"
        + "id="
        + id
        + ", email='"
        + email
        + '\''
        + ", name='"
        + name
        + '\''
        + ", role='"
        + role
        + '\''
        + ", delYn="
        + delYn
        + ", regDate="
        + regDate
        + ", udtDate="
        + udtDate
        + '}';
  }
}
