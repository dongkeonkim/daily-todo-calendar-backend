package com.dailytodocalendar.api.member.entity;

import com.dailytodocalendar.api.member.dto.MemberUpdateDto;
import com.querydsl.core.util.StringUtils;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private Long kakaoId;

  private String password;

  private String name;

  private String role;

  private Boolean delYn;

  private LocalDateTime regDate;

  private LocalDateTime udtDate;

  public void updateMember(MemberUpdateDto memberUpdateDto, PasswordEncoder passwordEncoder) {
    this.name = memberUpdateDto.getName();
    if (!StringUtils.isNullOrEmpty(memberUpdateDto.getNewPassword())) {
      this.password = passwordEncoder.encode(memberUpdateDto.getNewPassword());
    }
    this.udtDate = LocalDateTime.now();
  }

  public void changeDelYn(Boolean delYn) {
    this.delYn = delYn;
    this.udtDate = LocalDateTime.now();
  }

  public void setKakaoId(Long kakaoId) {
    this.kakaoId = kakaoId;
  }

  public static Member create(
      String email, String password, String name, PasswordEncoder passwordEncoder) {
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
