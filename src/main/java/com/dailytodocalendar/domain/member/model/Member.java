package com.dailytodocalendar.domain.member.model;

import com.dailytodocalendar.domain.common.event.DomainEventPublisher;
import com.dailytodocalendar.domain.member.event.MemberDeletedEvent;
import com.dailytodocalendar.domain.member.event.MemberUpdatedEvent;
import com.dailytodocalendar.domain.member.exception.MemberDomainException;
import java.time.LocalDateTime;
import lombok.*;

/** 회원 도메인 모델 - 도메인 핵심 엔티티 - 프레임워크와 독립적인 순수 자바 객체 (POJO) */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Member {

  private Long id;
  private Email email;
  private Long kakaoId;
  private Password password;
  private MemberName name;
  private String role;
  private boolean delYn;
  private LocalDateTime regDate;
  private LocalDateTime udtDate;

  /**
   * 회원 정보 업데이트
   *
   * @param newName 새 이름
   * @param newPassword 새 비밀번호 (변경하지 않을 경우 null)
   * @param eventPublisher 도메인 이벤트 발행자
   */
  public void updateMember(
      MemberName newName, Password newPassword, DomainEventPublisher eventPublisher) {
    this.name = newName;

    if (newPassword != null) {
      this.password = newPassword;
    }

    this.udtDate = LocalDateTime.now();

    // 도메인 이벤트 발행
    if (eventPublisher != null) {
      eventPublisher.publish(new MemberUpdatedEvent(this.id, this.email.getValue()));
    }
  }

  /**
   * 회원 삭제 처리
   *
   * @param eventPublisher 도메인 이벤트 발행자
   */
  public void markAsDeleted(DomainEventPublisher eventPublisher) {
    if (this.delYn) {
      throw new MemberDomainException("이미 삭제된 회원입니다.");
    }

    this.delYn = true;
    this.udtDate = LocalDateTime.now();

    // 도메인 이벤트 발행
    if (eventPublisher != null) {
      eventPublisher.publish(new MemberDeletedEvent(this.id, this.email.getValue()));
    }
  }

  /**
   * 카카오 회원 여부 확인
   *
   * @return 카카오 회원 여부
   */
  public boolean isKakaoMember() {
    return this.kakaoId != null;
  }

  /**
   * 존재하는 회원인지 확인
   *
   * @return 존재 여부 (삭제되지 않은 회원)
   */
  public boolean isActive() {
    return !this.delYn;
  }

  /**
   * 새 회원 생성 (팩토리 메서드)
   *
   * @param email 이메일
   * @param password 비밀번호
   * @param name 이름
   * @return 새 회원 객체
   */
  public static Member createNewMember(
      Email email, Password password, MemberName name, Long kakaoId) {
    return Member.builder()
        .email(email)
        .password(password)
        .name(name)
        .role("ROLE_USER")
        .kakaoId(kakaoId)
        .delYn(false)
        .regDate(LocalDateTime.now())
        .udtDate(LocalDateTime.now())
        .build();
  }
}
