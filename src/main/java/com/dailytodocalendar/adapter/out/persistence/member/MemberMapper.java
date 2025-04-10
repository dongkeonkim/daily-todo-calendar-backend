package com.dailytodocalendar.adapter.out.persistence.member;

import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import com.dailytodocalendar.domain.member.model.MemberName;
import com.dailytodocalendar.domain.member.model.Password;
import org.springframework.stereotype.Component;

/** 회원 매퍼 - JPA 엔티티와 도메인 모델 간의 변환 담당 */
@Component
public class MemberMapper {

  /**
   * JPA 엔티티를 도메인 모델로 변환
   *
   * @param jpaEntity JPA 엔티티
   * @return 도메인 모델
   */
  public Member toDomain(MemberJpaEntity jpaEntity) {
    return Member.builder()
        .id(jpaEntity.getId())
        .email(Email.of(jpaEntity.getEmail()))
        .password(Password.of(jpaEntity.getPassword()))
        .name(MemberName.of(jpaEntity.getName()))
        .kakaoId(jpaEntity.getKakaoId())
        .role(jpaEntity.getRole())
        .delYn(jpaEntity.getDelYn())
        .regDate(jpaEntity.getRegDate())
        .udtDate(jpaEntity.getUdtDate())
        .build();
  }

  /**
   * 도메인 모델을 JPA 엔티티로 변환
   *
   * @param domain 도메인 모델
   * @return JPA 엔티티
   */
  public MemberJpaEntity toJpaEntity(Member domain) {
    return MemberJpaEntity.builder()
        .id(domain.getId())
        .email(domain.getEmail().getValue())
        .password(domain.getPassword().getValue())
        .name(domain.getName().getValue())
        .kakaoId(domain.getKakaoId())
        .role(domain.getRole())
        .delYn(domain.isDelYn())
        .regDate(domain.getRegDate())
        .udtDate(domain.getUdtDate())
        .build();
  }
}
