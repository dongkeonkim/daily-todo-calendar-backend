package com.dailytodocalendar.api.member.entity;

import com.dailytodocalendar.api.member.dto.MemberUpdateDto;
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

    // 새 비밀번호가 제공된 경우에만 비밀번호 업데이트
    if (memberUpdateDto.getNewPassword() != null && !memberUpdateDto.getNewPassword().isEmpty()) {
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
