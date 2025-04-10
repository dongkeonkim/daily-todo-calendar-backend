package com.dailytodocalendar.application.port.in.member.dto;

import com.dailytodocalendar.domain.member.model.Member;
import java.time.LocalDateTime;
import lombok.Builder;

/** 회원 정보 DTO - 애플리케이션 계층에서 사용되는 회원 정보 객체 */
@Builder
public record MemberInfoDto(
    Long id,
    String email,
    boolean isKakaoUser,
    String name,
    LocalDateTime registeredAt,
    LocalDateTime lastUpdatedAt) {
  /**
   * 도메인 모델에서 DTO 생성
   *
   * @param member 회원 도메인 모델
   * @return 회원 정보 DTO
   */
  public static MemberInfoDto fromDomain(Member member) {
    return MemberInfoDto.builder()
        .id(member.getId())
        .email(member.getEmail().getValue())
        .isKakaoUser(member.isKakaoMember())
        .name(member.getName().getValue())
        .registeredAt(member.getRegDate())
        .lastUpdatedAt(member.getUdtDate())
        .build();
  }
}
