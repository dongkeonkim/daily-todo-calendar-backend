package com.dailytodocalendar.adapter.out.persistence.member;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/** 회원 JPA 엔티티 */
@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
public class MemberJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  private Long kakaoId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String role;

  @Column(nullable = false)
  private Boolean delYn;

  @Column(nullable = false)
  private LocalDateTime regDate;

  @Column(nullable = false)
  private LocalDateTime udtDate;
}
